<template>
	<div class="plugin_body">
		<div class=" div_form plugin_content">
			<el-form ref="step" size="mini" :model="step" :rules="rules" label-width="30%">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;" size="mini"></el-input>
				</el-form-item>
			</el-form>
			<div>
				<el-button style="float:right;margin-bottom: 5px;margin-right: 5px" type="primary" @click="addRow" size="mini">新增
				</el-button>
			</div>
			<el-table :data="step.fields.field" border height="330" :header-cell-style="{background:'#eef1f6'}">
				<el-table-column type="index" label="#" width="50"></el-table-column>
				<el-table-column prop="name" label="名称">
					<template slot-scope="scope">
						<el-select v-model="scope.row.name" size="mini" filterable allow-create clearable>
							<el-option v-for="item in dataName" :key="item.name" :label="item.name" :value="item.name"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="replaceby" label="替换为字段中的值">
					<template slot-scope="scope">
						<el-select v-model="scope.row.replaceby" size="mini" >
							<el-option v-for="item in replaceby" :key="item.name" :label="item.name" :value="item.name"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="100px">
					<template slot-scope="scope">
						<el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button @click="getDialog" size="mini">获取字段</el-button>
			<el-button type="primary" @click="submit" size="mini">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store.js";
	export default {
		data() {
			return {
				key: '', //插件对应的画布
				nodeData: {},
				dataName:[],
				replaceby:[],
				step: {
					name: "设置字段值",
					type: "SetValueField",
					description: [],
					distribute: "Y",
					custom_distribution: [],
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					fields: {
						field: []
					},
					filename: "",
					ftp_username: "",
					ftp_password: "",
					attributes: [],
					cluster_schema: [],
					remotesteps: {
						input: "\n      ",
						output: "\n      "
					},
					GUI: {
						xloc: 256,
						yloc: 96,
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

			}
		},
		mounted() {
			this.init();

		},
		methods: {
			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},

			addRow(event) { //新增一行
				this.step.fields.field.push({
					name: '',
					type: '',
					format: '',
					trim_type: '',
					length: -1,
					precision: -1,
					money: '',
					point: '',
					group: '',
				});
				this.curPage = parseInt(this.step.fields.field.length / this.pageSize + 1);
			},

			deleteRow(index) { //移除一行
				this.step.fields.field.splice(index, 1); //删掉该行
			},

			// getDialog() {
			// 	this.initi()
			// },
			getDialog() {
				this.step.fields.field = [];
				this.lastStepFields = JSON.parse(JSON.stringify(this.step['outFields']));
				this.lastStepFields.forEach(item => {
					this.step.fields.field.push({
						name: item.name,
						type: item.type,
						repeat: item.repeat,
						decimal:item.decimal,
						group:item.group,
						length:item.length,
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
				this.step.fields.field = [];
				let laststeps = store.getters.getLastStep(param);
				if (laststeps !== undefined && laststeps.length > 0) {
					this.laststep = laststeps[0];
				}

				//生成本步骤输出字段
				let outFields = store.getters.generateOutputFields(param);
				this.step['outFields'] = outFields;
				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				if (obj.initValid == undefined) {
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.dataName.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.replaceby.push({
								name: item.name,
							})
						});
					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.dataName.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.replaceby.push({
							name: item.name,
						})
					});
					this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
				}

				this.step.name = curstep.name;
			},

			submit() {
				this.step.initFlag = true;
				this.step.initValid = false;
				let step = Object.assign({}, this.step);
				if (this.step.header) {
					step.header = 'Y'
				} else {
					step.header = 'N'
				}
				if (this.step.newline_possible) {
					step.newline_possible = 'Y';
				} else {
					step.newline_possible = 'N';
				}
				step['oldStepName'] = this.oldStepName;
				// 控件重命名
				let paramName = step.name;
				let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
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
	.el-form-item--mini.el-form-item {
		margin-bottom: 10px;
	}
</style>
