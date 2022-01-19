<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="stepfrom" :model="step" :rules="rules" size="mini" label-width="30%">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;" size="mini"></el-input>
				</el-form-item>
			</el-form>
			<el-button style="float:right" type="primary" @click="addRow" size="mini">新增
			</el-button>
			<el-table :data="step.fields.field" border height="300" :header-cell-style="{background:'#eef1f6'}">
				<el-table-column type="index" label="#" width="50"></el-table-column>
				<el-table-column label="字段名称" prop="name">
					<template slot-scope="scope">
						<el-select v-model="scope.row.name" size="mini" clearable>
							<el-option v-for="item in name" :key="item.name" :label="item.name" :value="item.name"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="升序" prop="ascending">
					<template slot-scope="scope">
						<el-select v-model="scope.row.ascending" size="mini" clearable>
							<el-option v-for="item in ascending" :key="item.key" :label="item.label" :value="item.key"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button @click="getDialog" size="mini">获取字段</el-button>
			<el-button type="primary" @click="submit('stepfrom')" size="mini">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store.js";
	import {compareFields} from '../../../../assets/common/tool'

	export default {
		data() {
			return {
				key: '',
				nodeData: {},
				ascending: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				name: [],
				step: {
					description: [],
					distribute: "Y",
					custom_distribution: [],
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					attributes: [],
					cluster_schema: [],
					remotesteps: {
						input: "\n      ",
						output: "\n      "
					},
					GUI: {
						xloc: 200,
						yloc: 100,
						draw: "Y"
					},
					name: "排序合并",
					type: "SortedMerge",
					fields: {
						field:[]
					},
				},
				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						trigger: 'blur'
					}]
				},
			}
		},
		mounted() {
			this.init();
		},
		methods: {
			addRow(){
				this.step.fields.field.push({
					name: '',
					ascending:''
				})
			},
			deleteRow(index) {
				this.step.fields.field.splice(index, 1); //删掉该行
			},
			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},
			getDialog() {
				this.step.fields.field = [];
				this.lastStepFields = JSON.parse(JSON.stringify(this.step['outFields']));
				compareFields(this.lastStepFields);
				this.lastStepFields.forEach(item => {
					this.step.fields.field.push({
						name: item.name,
						type: item.type,
						repeat: item.repeat,
						decimal: item.decimal,
						group: item.group,
						length: item.length,
            ascending:'Y',
					})
				});
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

				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				if (obj.initValid == undefined) {

					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.name.push({
								name: item.name,
							})
              compareFields(this.name);
						});
					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.name.push({
							name: item.name,
						})
            compareFields(this.name);
					});
					this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
				}

				//判断是否是首次打开控件
				if (curstep !== undefined) {
					this.step.fileName = curstep.fileName;
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
			submit(stepfrom) {
				this.step.initFlag = true;
				this.step.initValid = false;
				let step = Object.assign({}, this.step);

				console.log(JSON.stringify(step));
				this.$refs[stepfrom].validate((valid) => {
					if (valid) {
						this.step['oldStepName'] = this.oldStepName;
						//校验是否有重复名称并获取重命名之后的名称
						let paramName = this.step.name;
						let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
						let flag = false;
						if (this.step.name !== newName) {
							flag = true;
							this.step.name = newName;
						}

						let param = {
							key: this.key,
							value: {
								oldStepName: this.oldStepName,
								step: this.step
							}
						};
						console.log(this.step);

						// 修改krt节点信息
						store.dispatch('updateStepInfo', param);
						let params = {
							id: this.nodeData.id, //插件id
							label: this.step.name,
							oldStepName: this.oldStepName
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
					} else {
						return false;
					}
				})
			},
		},
	}
</script>

<style>
</style>
