<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="stepfrom" :model="step" :rules="rules" size="mini" label-width="30%">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;" size="mini"></el-input>
				</el-form-item>
				<el-form-item label="使用的字段名" prop="field_to_use">
					<el-select v-model="step.field_to_use" size="mini" style="width: 65%;" clearable>
						<el-option v-for="item in field_to_use" :key="item.name" :label="item.name" :value="item.name"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="目标字段名(空=覆盖)" prop="target_field">
					<el-input v-model="step.target_field" style="width: 65%;" size="mini" clearable @blur="blurInput"></el-input>
				</el-form-item>
				<el-form-item label="不匹配时的默认值" prop="non_match_default">
					<el-input v-model="step.non_match_default" style="width: 65%;" size="mini" clearable></el-input>
				</el-form-item>
			</el-form>
			<el-button style="float:right" type="primary" @click="addRow" size="mini">新增
			</el-button>
			<el-table :data="step.fields.field" border height="300" :header-cell-style="{background:'#eef1f6'}">
				<el-table-column type="index" label="#" width="50"></el-table-column>
				<el-table-column prop="source_value" label="源值">
					<template slot-scope="scope">
						<el-input v-model="scope.row.source_value" size="mini" clearable></el-input>
					</template>
				</el-table-column>
				<el-table-column prop="target_value" label="目标值">
					<template slot-scope="scope">
						<el-input v-model="scope.row.target_value" size="mini" clearable></el-input>
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
			<el-button type="primary" @click="submit('stepfrom')" size="mini">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store.js";

	export default {
		data() {
			return {
				key: '',
				nodeData: {},
				field_to_use: [],

				step: {
					name: "值映射",
					type: "ValueMapper",
					fields: {
						field: []
					},
					field_to_use: "",
					target_field: "",
					non_match_default: "",
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
				},
				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						trigger: 'blur'
					}]
				},
				valueOutFields:[]
			}
		},
		mounted() {
			this.init();
		},
		methods: {

			blurInput() {
				let ret = 1;
				this.valueOutFields.forEach(item => {
					if (item.name === this.step.target_field) {
						this.step.target_field = this.step.target_field + '_' + ret;
            this.$message({message:'该字段名已存在，重命名为'+this.step.target_field,
              type:'warning'});
					}
				});
				this.step.fields.field.forEach(item => {
					if (item.name === this.step.target_field) {
						this.step.target_field = this.step.target_field + '_' + ret;
            this.$message({message:'该字段名已存在，重命名为'+this.step.target_field,
              type:'warning'});
					}
				});

			},
			//新增，删除
			addRow() {
				this.step.fields.field.push({
					source_value: '',
					target_value: ''
				})
			},
			deleteRow(index) {
				this.step.fields.field.splice(index, 1); //删掉该行
			},
			//关闭页面
			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
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
				this.valueOutFields = outFields;
				console.log(outFields);

				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				if (obj.initValid == undefined) {
					obj.initValid = false;
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.field_to_use.push({
								name: item.name,

							})
						});
						console.log(this.lastStepFields);
						console.log(111);

					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.field_to_use.push({
							name: item.name,
						})
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
					this.step.non_match_default = curstep.non_match_default;
					this.step.target_field = curstep.target_field;
					this.step.field_to_use = curstep.field_to_use === undefined ? '' : curstep.field_to_use;
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
						//新增字段
						if (this.step.target_field !== undefined && this.step.target_field !=='') {
							step.outFields.push({
								name: this.step.target_field,
								type: 'String',
								length: '255',
								precision: '-1',
								trim_type: "none",
								repeat: "N",
								format: "",
								currency: '¥',
								decimal: ".",
								group: ',',
							});
						}
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
