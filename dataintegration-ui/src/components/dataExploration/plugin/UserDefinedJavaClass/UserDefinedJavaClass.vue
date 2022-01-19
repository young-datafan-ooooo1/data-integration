<template>
	<div class="plugin_body">
		<div class="div_form  plugin_content">
			<el-form :model="step" ref="step" label-width="30%" :rules="rules" size="mini">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;"></el-input>
				</el-form-item>
			</el-form>
			<div class="father">
				<div class="rightson">
					<div>
						<span>类与代码片段</span>
					</div>
					<div class="right">
						<el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
					</div>
				</div>
				<div class="leftson">
					<div style="display: inline-block;padding-left:10px">
						<div>代码</div>
					</div>
					<div class="left">
						<el-tabs v-model="editableTabsValue" type="border-card" :tab-click="tabClick" editable>
							<el-tab-pane :key="item.class_type" v-for="(item, index) in step.definitions.definition" :label="item.class_name"
							 :name="item.class_type">
								<div class="java" contenteditable>
									<code-mirror ref="code"></code-mirror>
								</div>
							</el-tab-pane>
						</el-tabs>
					</div>
				</div>
			</div>
			<div>
				<el-tabs>
					<el-tab-pane label="字段">
						<div>
							<span>字段设置</span>
							<span style="float: right">
								<el-checkbox label="清空结果字段" v-model="step.clear_result_fields"></el-checkbox>
								<el-button size="mini" type="primary" @click="addRowfield" style="margin-right: 5px;">新增</el-button>
							</span>
						</div>
						<el-table :data="step.fields.field" height="200" :header-cell-style="{background:'#eef1f6'}">
							<el-table-column type="index" label="#" width="50"></el-table-column>
							<el-table-column prop="field_name" label="字段名">
								<template slot-scope="scope">
									<el-input v-model="scope.row.field_name" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="field_type" label="类型">
								<template slot-scope="scope">
									<el-select v-model="scope.row.field_type" size="mini" style="width: 100%;">
										<el-option v-for="item in field_type" :key="item" :label="item" :value="item">
										</el-option>
									</el-select>
								</template>
							</el-table-column>
							<el-table-column prop="field_length" label="长度">
								<template slot-scope="scope">
									<el-input v-model="scope.row.field_length" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="field_precision" label="精度">
								<template slot-scope="scope">
									<el-input v-model="scope.row.field_precision" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column label="操作" width="100px">
								<template slot-scope="scope">
									<el-button size="mini" type="text" @click="deleteRowfield(scope.$index)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
					<el-tab-pane label="参数">
						<el-button size="mini" type="primary" @click="addRowusage" style=" float:right;margin-right: 5px;">新增</el-button>
						<el-table :data="step.usage_parameters.usage_parameter" height="200" :header-cell-style="{background:'#eef1f6'}">
							<el-table-column type="index" label="#" width="50"></el-table-column>
							<el-table-column prop="parameter_tag" label="标签">
								<template slot-scope="scope">
									<el-input v-model="scope.row.parameter_tag" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="parameter_value" label="值">
								<template slot-scope="scope">
									<el-input v-model="scope.row.parameter_value" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="parameter_description" label="描述">
								<template slot-scope="scope">
									<el-input v-model="scope.row.parameter_description" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column label="操作" width="100px">
								<template slot-scope="scope">
									<el-button size="mini" type="text" @click="deleteRowusage(scope.$index)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
					<el-tab-pane label="消息步骤">
						<el-button size="mini" type="primary" @click="addRowinfo" style=" float:right;margin-right: 5px;">新增</el-button>
						<el-table :data="step.info_steps.info_step" height="200" :header-cell-style="{background:'#eef1f6'}">
							<el-table-column type="index" label="#" width="50"></el-table-column>
							<el-table-column prop="step_tag" label="标签">
								<template slot-scope="scope">
									<el-input v-model="scope.row.step_tag" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="step_name" label="步骤">
								<template slot-scope="scope">
									<el-select v-model="scope.row.step_name" size="mini" style="width: 100%;">
										<el-option v-for="item in info_step_name" :key="item" :label="item" :value="item">
										</el-option>
									</el-select>
								</template>
							</el-table-column>
							<el-table-column prop="step_description" label="描述">
								<template slot-scope="scope">
									<el-input v-model="scope.row.step_description" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column label="操作" width="100px">
								<template slot-scope="scope">
									<el-button size="mini" type="text" @click="deleteRowinfo(scope.$index)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
					<el-tab-pane label="目标步骤">
						<el-button size="mini" type="primary" @click="addRowtarget" style=" float:right;margin-right: 5px;">新增</el-button>
						<el-table :data="step.target_steps.target_step" height="200" :header-cell-style="{background:'#eef1f6'}">
							<el-table-column type="index" label="#" width="50"></el-table-column>
							<el-table-column prop="step_tag" label="标签">
								<template slot-scope="scope">
									<el-input v-model="scope.row.step_tag" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="step_name" label="步骤">
								<template slot-scope="scope">
									<el-select v-model="scope.row.step_name" size="mini" style="width: 100%;">
										<el-option v-for="item in target_step_name" :key="item" :label="item" :value="item">
										</el-option>
									</el-select>
								</template>
							</el-table-column>
							<el-table-column prop="step_description" label="描述">
								<template slot-scope="scope">
									<el-input v-model="scope.row.step_description" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column label="操作" width="100px">
								<template slot-scope="scope">
									<el-button size="mini" type="text" @click="deleteRowtarget(scope.$index)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
					</el-tab-pane>
				</el-tabs>
			</div>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer" align="right">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button @click="test = false" size="mini">测试类</el-button>
			<el-button type="primary" @click="submit" size="mini">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store";
	import codeMirror from "../../../common/codemirror.vue";
	import RowGenerator from "../RowGenerator/RowGenerator";


	export default {
		data() {
			return {
				field_type: ["Binary", "Number", "String", "Date", "Boolean", "Integer", "Internet Address", "BigNumber",
					"Timestamp"], //字段类型
				nodeData: '',
				key: '',
				editableTabsValue: '0',
				data: [{
					id: 1,
					label: '类',
					children: [{}]
				}, {
					id: 2,
					label: 'Code Snippits',
					children: [{}]
				}, {
					id: 3,
					label: 'Input Fields',
					children: [{}]
				}, {
					id: 4,
					label: 'Info Fields',
					children: [{}]
				}, {
					id: 5,
					label: 'Output Fields',
					children: [{}],
				}],
				defaultProps: {
					children: 'children',
					label: 'label'
				},
				target_step_name: [],
				info_step_name: [],
				step: {
					name: "Java代码",
					type: "UserDefinedJavaClass",
					description: "",
					distribute: "Y",
					custom_distribution: "",
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: ""
					},
					definitions: {
						definition: [{
							class_type: "0",
							class_name: "脚本1",
							class_source: "在此处编写脚本"
						}]
					},
					clear_result_fields: false,
					fields: {
						field: []
					},
					info_steps: {
						info_step: []
					},
					target_steps: {
						target_step: []
					},
					usage_parameters: {
						usage_parameter: []
					},
					cluster_schema: [],
					remotesteps: {
						input: "\n      ",
						output: "\n      "
					},
					GUI: {
						xloc: "96",
						yloc: "144",
						draw: "Y"
					}
				},
				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						trigger: 'blur'
					}]
				},
			};
		},
		components: {
			codeMirror,
			RowGenerator
		},

		mounted() {
			this.init();
		},
		methods: {

			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},
			addRowfield() {
				this.step.fields.field.push({
					field_name: "",
					field_type: "",
					field_precision: "",
					field_length: "",
				})
			},
			addRowusage() {
				this.step.usage_parameters.usage_parameter.push({
					parameter_tag: "",
					parameter_value: "",
					parameter_description: "",
				})
			},
			addRowinfo() {
				this.step.info_steps.info_step.push({
					step_name: "",
					step_tag: "",
					step_description: "",
				})
			},
			addRowtarget() {
				this.step.target_steps.target_step.push({
					step_name: "",
					step_description: "",
					step_tag: "",
				})
			},
			deleteRowfield(index) {
				this.step.fields.field.splice(index, 1); //删掉该行
			},
			deleteRowusage(index) {
				this.step.usage_parameters.usage_parameter.splice(index, 1); //删掉该行
			},
			deleteRowinfo(index) {
				this.step.info_steps.info_step.splice(index, 1); //删掉该行
			},
			deleteRowtarget(index) {
				this.step.target_steps.target_step.splice(index, 1); //删掉该行
			},

			handleNodeClick() {

			},
			tabClick() {

			},
			init() {
				this.step.name = this.nodeData.label;
				this.oldStepName = this.nodeData.label;
				let stepName = this.step.name;
				//获取当前步骤信息
				let params = {
					key: this.key,
					value: stepName,
				};
				let curStep = store.getters.getCurrentStep(params);
				let outFields = store.getters.generateOutputFields(params);
				this.step['outFields'] = outFields;
				let laststeps = store.getters.getLastStep(params);
				if (laststeps !== undefined && laststeps.length > 0) {
					this.laststep = laststeps[0];
				}
				if (curStep !== undefined) {
					this.step.name = curStep.name;
					this.step.initFlag = curStep.initFlag;
					this.step.description = curStep.description;
					this.step.distribute = curStep.distribute;
					this.step.copies = curStep.copies;
					this.step.partitioning = curStep.partitioning;
					this.step.model_name = curStep.model_name;
					this.step.remotesteps = curStep.remotesteps;
					this.step.GUI = curStep.GUI;
					this.step.definitions.definition = curStep.definitions.definition;
					this.step.fields.field = curStep.fields.field;
					this.step.usage_parameters.usage_parameter = curStep.usage_parameters.usage_parameter;
					this.step.info_steps.info_step = curStep.info_steps.info_step;
					this.step.target_steps.target_step = curStep.target_steps.target_step;
					this.step.clear_result_fields = (curStep.clear_result_fields === 'Y' || curStep.clear_result_fields === undefined);

				}

				let javaData = this.step.definitions;
				console.info(javaData);
				let context = javaData.definition[0].class_source;
				console.info(context);
				this.$nextTick(() => {
					this.$refs.code[0].editor.setValue(context);
				})
			},
			submit() {

				this.$refs.code.forEach((item, index) => {
					console.log(item.editor.getValue());
					this.step.definitions.definition[index].class_source = item.editor.getValue();
				});
				let step = Object.assign({}, this.step);
				if (this.step.clear_result_fields == true) {
					step.clear_result_fields = 'Y'
				} else {
					step.clear_result_fields = 'N'
				}
				// for (let key in step) {
				//   if (typeof step[key] === 'boolean') {
				//     step[key] = step[key] === 'Y';
				//   }
				// }
				// this.$refs.code.forEach((item, index) => {
				//   this.step.jsScripts.jsScript[index].jsScript_script = item.editor.getValue();
				// })
				step.initFlag = true;
				step["oldStepName"] = this.oldStepName;
				// 控件重命名
				let paramName = step.TextName;
				let newName = store.getters.getCheckNodeName(
					this.key,
					this.oldStepName,
					paramName
				);
				// step.fields.field.forEach(item => {
				//   step.outFields.push({
				//     name: item.name,
				//     type: item.type,
				//     length: item.length,
				//     precision: item.precision,
				//     trim_type: "none",
				//     repeat: "N",
				//     format: "",
				//     currency: item.currency === '' ? '¥' : item.currency,
				//     decimal: item.decimal === '' ? "." : item.decimal,
				//     group: item.group === '' ? ',' : item.group
				//   })
				// })

				let flag = false;
				if (step.TextName !== newName) {
					flag = true;
					step.TextName = newName;
				}
				// 修改步骤xinxi
				let param = {
					key: this.key,
					value: {
						oldStepName: this.oldStepName,
						step: step
					}
				};

				console.info("最终修改的步骤名称", step);
				store.dispatch("updateStepInfo", param);
				let params = {
					id: this.nodeData.id, //插件id
					label: step.name,
					oldName: this.oldStepName
				};
				util.$emit("updateNode", params);
				util.$emit("closeDialog", "close");
				if (flag) {
					this.$alert(paramName + "名称已存在,重命名为" + newName + "!", "注释", {
						confirmButtonText: "确定"
					});
				} else {
					this.$message({
						message: "步骤信息修改成功",
						type: "success"
					});
				}
			},
		},
	}
</script>

<style>
	.father {
		display: inline-block;
		width: 100%;
	}

	.rightson {
		float: left;
		width: 40%;
	}

	.right {
		height: 200px;
		margin-right: 5px;
		border: 1px solid #999;
		overflow: auto;
	}

	.leftson {
		float: right;
		width: 60%;
	}

	.left {
		/*border: 1px solid #999;*/
		height: 200px;
		overflow: hidden;
		border-radius: 2%;
		margin-left: 10px;
	}

	.el-menu-item {
		font-size: 12px;
		color: #999;
	}

	.foot {
		margin-left: 68%;
		margin-bottom: 10px;
		position: absolute;
	}
</style>
