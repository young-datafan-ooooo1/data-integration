<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
				<el-form ref="step" :model="step" :rules="rules" size="mini" label-width="30%">
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
				<el-table-column prop="in_stream_name" label="输入流字段">
					<template slot-scope="scope">
						<el-select v-model="scope.row.in_stream_name" size="mini" filterable allow-create clearable>
							<el-option v-for="item in in_stream_name" :key="item.name" :label="item.in_stream_name" :value="item.in_stream_name"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="out_stream_name" label="输出流字段">
					<template slot-scope="scope">
						<el-input v-model="scope.row.out_stream_name" size="mini" clearable></el-input>
					</template>
				</el-table-column>
				<el-table-column prop="cut_from" label="起始位置">
					<template slot-scope="scope">
						<el-input v-model="scope.row.cut_from" size="mini"></el-input>
					</template>
				</el-table-column>
				<el-table-column prop="cut_to" label="结束位置">
					<template slot-scope="scope">
						<el-input v-model="scope.row.cut_to" size="mini"></el-input>
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
				in_stream_name: [],
				step: {
					name: "剪切字符串",
					type: "StringCut",
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
						yloc: 128,
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
				//this.selectedPage();
			},

			deleteRow(index) { //移除一行
				// let Index = (this.curPage - 1) * this.pageSize + index
				this.step.fields.field.splice(index, 1); //删掉该行
				//this.selectedPage();
			},

			// getDialog() {
			// 	this.initi()
			// },

			getDialog() {
				this.step.fields.field = [];
				this.lastStepFields = JSON.parse(JSON.stringify(this.step['outFields']));
				this.lastStepFields.forEach(item => {
					this.step.fields.field.push({
						in_stream_name: item.name,
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
				//获取本步骤信息
				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				if (obj.initValid == undefined) {
					obj.initValid = false;
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.in_stream_name.push({
								in_stream_name: item.name,
							})
						});

					}
					// this.stringCutData = this.laststep.fields.field;

				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.in_stream_name.push({
							in_stream_name: item.name,
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
		}
	}
</script>

<style>
	.el-form-item--mini.el-form-item {
		margin-bottom: 10px;
	}
</style>
