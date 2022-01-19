<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" size="mini" style="width: 65%;"></el-input>
				</el-form-item>
			</el-form>
			<div>
				<el-button style="float:right;margin-bottom: 5px;margin-right: 5px" type="primary" @click="addRow" size="mini">新增
				</el-button>
			</div>
			<el-table :data="step.steps.step" border height="330" :header-cell-style="{background:'#eef1f6'}">
				<el-table-column type="index" label="#" width="50"></el-table-column>
				<el-table-column prop="name" label="步骤名称">
					<template slot-scope="scope">
						<el-select v-model="scope.row.name" size="mini" clearable>
							<el-option v-for="item in name " :key="item.name" :label="item.name" :value="item.name"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="CopyNr" label="复制次数">
					<template slot-scope="scope">
						<el-input v-model="scope.row.CopyNr" size="mini" clearable></el-input>
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer" align="right">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button @click="getStep" size="mini">获取步骤</el-button>
			<el-button type="primary" @click="submit" size="mini">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from '../../../../vuex/store.js';

	export default {
		data() {
			return {
				key: '',
				nodeData: {},
				name: [],
				step: {
					name: "阻塞数据直到步骤都完成",
					type: "BlockUntilStepsFinish",
					description: [],
					distribute: "Y",
					custom_distribution: [],
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					steps: {
						step:[]
					},
					attributes: [],
					cluster_schema: [],
					remotesteps: {
						input: "\n      ",
						output: "\n      "
					},
					GUI: {
						xloc: "304",
						yloc: "48",
						draw: "Y"
					}
				},
				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						tigger: 'blur'
					}]
				},
				until: [],
			}
		},
		mounted() {
			this.init()
		},
		methods: {

			//新增
			addRow() {
				this.step.steps.step.push({
					name: '',
					CopyNr: '0'
				})
			},
			deleteRow(index) { //移除一行
				this.step.steps.step.splice(index, 1); //删掉该行
			},

			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},

			//获取步骤
			getStep() {
				this.step.steps.step = [];
				this.blockStep.forEach(item => {
					this.until.forEach(row => {
						if (item.name !== row.name && item.name !== row.nameto) {
							this.step.steps.step.push({
								name: item.name,
								CopyNr: '0',
							})
						}
					})
				})
			},

			init() {
				this.step.name = this.nodeData.label;
				this.oldStepName = this.nodeData.label;
				let stepName = this.step.name;
				// 获取当前步骤信息参数
				let param = {
					key: this.key, //用于标记画布，这里用的是画布路径
					value: stepName
				};
				let laststeps = store.getters.getLastStep(param);
				if (laststeps !== undefined && laststeps.length > 0) {
					this.laststep = laststeps[0];
				}


				//生成本步骤输出字段
				let outFields = store.getters.generateOutputFields(param);
				this.step['outFields'] = outFields;
				console.log(outFields);

				//获取步骤
				// let blockStep = store.state.flowMain.flowDataJson[this.key].transformation;
				// console.log(blockStep);
				let blockSteps = store.state.flowMain.stepList[this.key];
				this.blockStep = blockSteps;
				console.log(this.blockStep);
				let blockLines = store.state.flowMain.lines[this.key];
				this.blockLine = blockLines;
				console.log(this.blockLine);




				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				console.log(curstep);

				if (obj.initValid == undefined) {
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						console.log(this.lastStepFields);
						console.log(111);
					}
					this.blockLine.forEach(row => {
						if (row.from == curstep.name) {
							this.until.push({
								name: row.from,
								nameto: row.to
							})
						}
					});
					this.blockStep.forEach(item => {
						this.until.forEach(row => {
							if (item.name !== row.name && item.name !== row.nameto) {
								console.log(item.name);
								this.name.push({
									name: item.name
								})
							}
						})
					});
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));

					this.blockLine.forEach(row => {
						console.log(row.from);
						if (row.from == curstep.name) {
							this.until.push({
								name: row.from,
								nameto: row.to
							})
						}
					});
					this.blockStep.forEach(item => {
						this.until.forEach(row => {
							if (item.name !== row.name && item.name !== row.nameto) {
								this.name.push({
									name: item.name
								})
							}
						})
					});
					this.step.steps.step = JSON.parse(JSON.stringify(obj.steps.step));
				}
				console.log(this.step.steps.step);
				//判断是否是首次打开控件
				if (curstep !== undefined) {
					this.step.name = curstep.name;
					this.step.initFlag = curstep.initFlag;
					this.step.description = curstep.description;
					this.step.distribute = curstep.distribute;
					this.step.copies = curstep.copies;
					this.step.partitioning = curstep.partitioning;
					this.step.model_name = curstep.model_name;
					this.step.remotesteps = curstep.remotesteps;
					this.step.GUI = curstep.GUI;
				}
			},

			submit() {
				this.step.initFlag = true;
				this.step.initValid = false;
				console.info("最终字段", this.step);
				let step = Object.assign({}, this.step);

				// 控件重命名
				let paramName = step.name;
				let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
				console.info(newName);
				let flag = false;
				if (step.name !== newName) {
					flag = true;
					step.name = newName;
				}
				// 修改步骤xinxi
				let param = {
					key: this.key,
					value: {
						oldStepName: this.oldStepName,
						step: step
					}
				};
				console.info("step,-=-=-", step);
				store.dispatch('updateStepInfo', param);
				let params = {
					id: this.nodeData.id, //插件id
					label: step.name,
					oldName: this.oldStepName,
				};
				util.$emit('updateNode', params);
				util.$emit('closeDialog', 'close');
				if (flag) {
					this.$alert(paramName + '名称已存在,重命名为' + newName + "!", '注释', {
						confirmButtonText: '确定',
					});
				} else {
					this.$message({
						message: '步骤信息修改成功',
						type: 'success'
					})
				}
			},
		},
	}
</script>

<style>
</style>
