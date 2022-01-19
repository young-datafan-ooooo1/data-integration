<template>
	<div class="plugin_body">
		<div class="plugin_content div_form ">
			<el-form ref="stepfrom" class="title_div" :model="step" size="mini" :rules="rules" label-width="30%">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;" size="mini"></el-input>
				</el-form-item>
				<el-form-item label="第一个步骤" prop="step1">
					<el-select v-model="step.step1" style="width: 65%;" @change="selectLeft" size="mini" filterable>
						<el-option v-for="item in lastSteps" :key="item.name" :label="item.name" :value="item.name"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="第二个步骤" prop="step2">
					<el-select v-model="step.step2" style="width: 65%;" size="mini" @change="selectRight" filterable>
						<el-option v-for="item in lastSteps" :key="item.name" :label="item.name" :value="item.name"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="连接类型" prop="type">
					<el-select v-model="step.join_type" style="width: 65%;" size="mini" filterable>
						<el-option v-for="item in joinTypes" :key="item" :label="item" :value="item"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<div>
				<div style="float: left; width: calc(40% - 10px);margin-right: 10px; margin-left: 10%;">
					<div>
						<div style="width: 50%;display: inline-block;">第一个步骤的连接字段</div>
						<div style="display: inline-block;float: right;">
							<el-button class="getjoin" type="primary"  size="mini" @click="addRow">新增连接字段</el-button>
						</div>
					</div>
					<el-table :data="step.keys_1" height="330" border :header-cell-style="{background:'#eef1f6'}">
						<el-table-column type="index" label="#"></el-table-column>
						<el-table-column prop="key" label="连接字段">
							<template slot-scope="scope">
								<el-select v-model="scope.row.key" clearable placeholder="选择字段" @change="checkFieldsTypeL(scope.$index)" size="mini"
								 style="width: 100%" filterable allow-create>
									<el-option v-for="item in leftFields" :key="item.name" :label="item.name" :value="item.name">
									</el-option>
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
				<div style="display: inline-block;	width: 40%; ">
					<div>
						<div style="width: 50%;display: inline-block;">第二个步骤的连接字段</div>
						<div style="5display: inline-block;float: right;">
							<el-button class="getjoin" type="primary"  size="mini" @click="addRow1">新增连接字段</el-button>
						</div>
					</div>
					<el-table :data="step.keys_2" height="330" border :header-cell-style="{background:'#eef1f6'}">
						<el-table-column type="index" label="#" width="50"></el-table-column>
						<el-table-column prop="key" label="连接字段">
							<template slot-scope="scope">
								<el-select v-model="scope.row.key" clearable placeholder="选择字段" @change="checkFieldsTypeR(scope.$index)" size="mini"
								 style="width: 100%" filterable allow-create>
									<el-option v-for="item in rightFields" :key="item.name" :label="item.name" :value="item.name">
									</el-option>
								</el-select>
							</template>
						</el-table-column>
						<el-table-column label="操作" width="100px">
							<template slot-scope="scope">
								<el-button size="mini" type="text" @click="deleteRow1(scope.$index)">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
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
				lastSteps: [],
				key: '', //插件对应的画布
				nodeData: {},
				leftFields: [],
				rightFields: [],
				step: {
					initFlag: true,
					name: "记录集连接",
					type: "MergeJoin",
					description: [],
					distribute: "Y",
					custom_distribution: [],
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					join_type: "INNER",
					step1: "",
					step2: "",
					keys_1: [],
					keys_2: [],
					filename: "",
					attributes: [],
					fields: {
						field: []
					},
					cluster_schema: [],
					remotesteps: {
						input: "\n ",
						output: "\n  "
					},
					GUI: {
						xloc: 304,
						yloc: 80,
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
				joinTypes: ["INNER", "LEFT OUTER", "RIGHT OUTER", "FULL OUTER"],
				fieldsMer: []
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

			deleteRow(index) { //移除一行
				this.step.keys_1.splice(index, 1); //删掉该行

			},

			addRow() {
					this.step.keys_1.push({
						key: '',
					});
			},
			deleteRow1(index) { //移除一行
				this.step.keys_2.splice(index, 1); //删掉该行
			},

			addRow1() {
					this.step.keys_2.push({
						key: '',
					});
			},


			pushFields(item) {
				console.info("item-=-=", item);
				if (item !== undefined && item !== null) {
					item.fields.field.forEach((item1, index) => {
						let fieldName = item1.name;
						let ret = 1;
						while (this.checkFieldExits(fieldName)) {
							fieldName = item1.name;
							fieldName = fieldName + '_' + ret;
							ret++;
						}
						console.info("fileName", fieldName);

						this.step.fields.field.push({
							name: fieldName,
							type: item1.type,
							format: item1.format,
							currency: item1.currency,
							decimal: item1.decimal,
							group: item1.group,
							nullif: item1.nullif,
							trim_type: item1.name,
							length: item1.length,
							precision: item1.precision,
							description: item1.description
						});
						console.info("-=-=-=-=", this.step.fields.field);
						this.step.outFields = this.step.fields.field;
					})
				}
			},
			
			checkFieldExits(fieldName) {
				for (let i = 0; i < this.step.fields.field.length; i++) {
					if (fieldName === this.step.fields.field[i].name) {
						return true;
					}
				}
				return false;
			},
			

			selectLeft() {
				this.leftFields = [];
				let selStep = undefined;
				if (this.step.step1 !== null && this.step.step1 !== '') {
					this.lastSteps.forEach((item, index) => {
						if (item.name === this.step.step1) {
							selStep = item;
						}
					});
				}
				console.log(selStep);
				if (selStep != undefined) {
					this.fieldsMer.forEach(item => {
						if (item.name == selStep.name) {
							this.leftFields = item.fileds;
						}
					})
					// let selStepFields =
					// selStepFields.forEach((item, index) => {
					// 	this.leftFields.push(item);
					// });
				}


			},
			selectRight() {
				this.rightFields = [];
				let selStep = undefined;
				if (this.step.step2 !== null && this.step.step2 !== '') {
					this.lastSteps.forEach((item, index) => {
						if (item.name === this.step.step2) {
							selStep = item;
						}
					});
				}
				console.log(selStep);
				if (selStep != undefined) {
					this.fieldsMer.forEach(item => {
						if (item.name == selStep.name) {
							this.rightFields = item.fileds;
						}
					})
				}

			},

			checkFieldsTypeL(index) {

				console.log(this.step.keys_1[index]);
				console.log(this.step.keys_2[index]);
				let lType = undefined;
				let rType = undefined;


				let key1 = this.step.keys_1[index];
				let key2 = this.step.keys_2[index];

				if (key1 != undefined) {
					this.leftFields.forEach((item, index) => {
						if (key1.key === item.name) {
							lType = item.type;
						}
					});
				}

				if (key2 != undefined) {
					this.rightFields.forEach((item, index) => {
						if (key2.key === item.name) {
							rType = item.type
						}
					});
				}

				if (lType !== undefined && lType !== '' && rType !== undefined && rType !== '' && lType !== rType) {
					this.$message({
						message: '数据类型不匹配',
						type: 'error'
					});
				}


			},
			checkFieldsTypeR(index) {

				let lType = undefined;
				let rType = undefined;


				let key1 = this.step.keys_1[index];
				let key2 = this.step.keys_2[index];

				if (key1 != undefined) {
					this.leftFields.forEach((item, index) => {
						if (key1.key === item.name) {
							lType = item.type;
						}
					});
				}

				if (key2 != undefined) {
					this.rightFields.forEach((item, index) => {
						if (key2.key === item.name) {
							rType = item.type
						}
					});
				}

				if (lType !== undefined && lType !== '' && rType !== undefined && rType !== '' && lType !== rType) {
					this.$message({
						message: '数据类型不匹配',
						type: 'error'
					});
				}


			},

			init() {
				this.step.name = this.nodeData.label;
				this.oldStepName = this.nodeData.label;
				let stepName = this.step.name;
				let param = {
					key: this.key, //用于标记画布，这里用的是画布路径
					value: stepName
				};
				//获取上一步骤信息
				this.lastSteps = store.getters.getLastStep(param);
				console.log(this.lastSteps);

				//生成本步骤输出字段
				let outFields = store.getters.generateOutputFields(param);
				this.step['outFields'] = outFields;
				console.log(outFields);

				//记录集获取字段
				let Fields = store.getters.generateMergeJoin(param);
				this.fieldsMer = Fields;
				console.log(Fields);

				console.log(this.step.keys_2);
				console.log(this.lastSteps);
				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				if (obj.initValid == undefined) {

					if (curstep !== undefined && curstep !== null && curstep.step1 !== undefined) {
						this.step.step1 = curstep.step1;
						this.step.step2 = curstep.step2;
						this.step.join_type = curstep.join_type === undefined ? 'INNER' : curstep.join_type;
						this.step.keys_1 = [];
						for (let i = 0; i < curstep.keys_1.length; i++) {
							this.step.keys_1.push({
								key: curstep.keys_1[i].key,
							})
						}
						console.log(this.step.keys_1);
						this.step.keys_2 = [];
						for (let i = 0; i < curstep.keys_2.length; i++) {
							this.step.keys_2.push({
								key: curstep.keys_2[i].key,
							})
						}
						console.log(this.step.keys_2);
						this.selectRight();
						this.selectLeft();
					}
				} else {
					this.step.name = curstep.name;
					this.step.step1 = curstep.step1;
					this.step.step2 = curstep.step2;
					this.step.join_type = curstep.join_type;
					this.step.keys_1 = curstep.keys_1;
					this.step.keys_2 = curstep.keys_2;
					this.selectRight();
					this.selectLeft();
				}
			},

			submit(stepfrom) {
				this.step.initFlag = true;
				this.step.initValid = false;
				console.info("最终字段", this.step);
				this.$refs[stepfrom].validate((valid) => {
					if (valid) {
						this.step['oldStepName'] = this.oldStepName;
						//this.step.conditions = [];
						/* this.step.keys_1 = [];
						this.step.keys_2 = [];
						this.step.keys_1.forEach((item, index) => {
							console.log(item);
							this.step.keys_1.push({
								key: item.keys_1.key
							});
						});
						console.log(this.step.keys_1);
						this.step.keys_2.forEach((item, index) => {
							console.log(item);
							this.step.keys_2.push({
								key: item.keys_2.key
							});
						});
						console.log(this.step.keys_2); */
						let obj1 = {};
						obj1 = this.lastSteps.find((item) => {
							return item.name === this.step.step1; //筛选出匹配数据
						});
						this.pushFields(obj1);

						let obj2 = {};
						obj2 = this.lastSteps.find((item) => {
							return item.name === this.step.step2; //筛选出匹配数据
						});
						this.pushFields(obj2);

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
						console.log('error submit!!');
						return false;
					}
				})
			},

		},
	}
</script>
<style>
	.getjoin {
		margin-bottom: 5px;
	}

	.el-form-item--mini.el-form-item {
		margin-bottom: 10px;
	}
</style>
