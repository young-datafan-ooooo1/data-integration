<template>
	<div class="plugin_body">
		<div class="div_form  plugin_content plugin_form1">
			<el-form ref="step" :model="step" size="mini" :rules="rules" label-width="30%">
				<el-form-item label="作业名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;" size="mini"></el-input>
				</el-form-item>
				<el-divider content-position="left">替换所有字段的null值</el-divider>
				<el-form-item label="值替换为">
					<el-input v-model="step.replaceAllByValue" style="width:65%;" size="mini" filterable :disabled="replace"></el-input>
				</el-form-item>
				<el-form-item label="设置空字符串">
					<el-checkbox v-model="step.setEmptyStringAll" :disabled="setString"></el-checkbox>
				</el-form-item>
				<el-form-item label="(掩码)日期">
					<el-select v-model="step.replaceAllMask" style="width:65%;" size="mini" filterable :disabled="msak">
						<el-option v-for="item in replaceAllMask" :key="item" :label="item" :value="item"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="选择字段">
					<el-checkbox v-model="step.selectFields" @change="changefield('0')"></el-checkbox>
				</el-form-item>
        <el-form-item label="选择值类型">
          <el-checkbox  v-model="step.selectValuesType" @change="changefield('1')"></el-checkbox>
        </el-form-item>
			</el-form>
			<div>
				<div class="node_title_basic" ><span>值类型</span>
					<el-button style="float:right" type="primary" @click="addRowvalue" size="mini" :disabled="tablevalue">新增
					</el-button>
				</div>
				<el-table :data="step.valuetypes.valuetype" border height="330" :header-cell-style="{background:'#eef1f6'}">
					<el-table-column type="index" label="#" width="50"></el-table-column>
					<el-table-column prop="name" label="类型">
						<template slot-scope="scope">
							<el-select v-model="scope.row.name" size="mini" style="width: 100%;" clearable :disabled="tablevalue">
								<el-option v-for="item in valuename" :key="item" :label="item" :value="item">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="value" label="值替换为">
						<template slot-scope="scope">
							<el-input v-model="scope.row.value" size="mini" :disabled="tablevalue"></el-input>
						</template>
					</el-table-column>
					<el-table-column prop="mask" label="转换掩码(日期)">
						<template slot-scope="scope">
							<el-select v-model="scope.row.mask" size="mini" style="width: 100%;" clearable :disabled="tablevalue">
								<el-option v-for="item in valuemask" :key="item" :label="item" :value="item">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="set_type_empty_string" label="设置空字符串">
						<template slot-scope="scope">
							<el-select v-model="scope.row.set_type_empty_string" size="mini" style="width: 100%;" clearable :disabled="tablevalue">
								<el-option v-for="item in set_type_empty_string" :key="item.key" :label="item.label" :value="item.key">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column label="操作" width="100px">
						<template slot-scope="scope">
							<el-button size="mini" type="text" @click="deleteRowvalue(scope.$index)" :disabled="tablevalue">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<div style="margin-top: 10px;">
				<div><span>字段</span>
					<el-button style="float:right" type="primary" @click="addRowfield" size="mini" :disabled="tablefield">新增
					</el-button>
				</div>
				<el-table :data="step.fields.field" border height="330" :header-cell-style="{background:'#eef1f6'}">
					<el-table-column type="index" label="#" width="50"></el-table-column>
					<el-table-column prop="name" label="字段">
						<template slot-scope="scope">
							<el-select v-model="scope.row.name" size="mini" style="width: 100%;" clearable :disabled="tablefield">
								<el-option v-for="item in name" :key="item.name" :label="item.name" :value="item.name">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="value" label="值替换为">
						<template slot-scope="scope">
							<el-input v-model="scope.row.value" size="mini" :disabled="tablefield"></el-input>
						</template>
					</el-table-column>
					<el-table-column prop="mask" label="转换掩码(日期)">
						<template slot-scope="scope">
							<el-select v-model="scope.row.mask" size="mini" style="width: 100%;" clearable :disabled="tablefield">
								<el-option v-for="item in mask" :key="item" :label="item" :value="item">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="set_empty_string" label="设置空字符串">
						<template slot-scope="scope">
							<el-select v-model="scope.row.set_empty_string" size="mini" style="width: 100%;" clearable :disabled="tablefield">
								<el-option v-for="item in set_empty_string" :key="item.key" :label="item.label" :value="item,key">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column label="操作" width="100px">
						<template slot-scope="scope">
							<el-button size="mini" type="text" @click="deleteRowfield(scope.$index)" :disabled="tablefield">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer">
			<el-button @click="closeDialog" size="small">取 消</el-button>
			<el-button @click="getDialog" size="small" :disabled="get">获取字段</el-button>
			<el-button type="primary" @click="submit" size="small">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store.js";

	export default {
		data() {
			return {
				get: true,
				tablefield: true,
				tablevalue: true,
				replace: false,
				setString: false,
				msak: false,
				key: '', //插件对应的画布
				nodeData: {},
				step: {
					name: "替换Null值",
					type: "IfNull",
					distribute: "Y",
					copies: 1,
					partitioning: {
						method: "none"
					},
					replaceAllMask: "",
					replaceAllByValue: "",
					selectFields: false,
					selectValuesType: false,
					setEmptyStringAll: false,
					valuetypes: {
						valuetype: []
					},
					fields: {
						field: []
					},
					remotesteps: {
						input: "",
						output: ""
					},
					GUI: {
						xloc: "592",
						yloc: "48",
						draw: "Y"
					}
				},
				encodings: ["yyyy/MM/dd HH:mm:ss.Sss", "yyyy/MM/dd HH:mm:ss.SSS xxx", "yyyy/MM/dd HH:mm:ss",
					"yyyy/MM/dd HH:mm:ss XXx",
					"yyyyMMddHHmmss", "yyyy/MM/dd", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss XXx", "yyyyMMdd",
					"MM/dd/yyyyMM/ddlyyyy HH:mm:ss", "MM-dd-yyyy", "MM-dd-yyyy HH:mm:ss", "MM/dd/yy", "MM-dd-yy", "dd/MM/yyyy",
					"dd-MM-yyyy", "xy-MM.-dd", "HH:mm:ss.SSSXXX", "wy-MIM-da HH:mm:ss.sss"
				],
				name: [],
				valuename: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Serializable", "Binary", "Timestamp",
					"Internet Address"
				],
				mask: [
					"yyyy/MM/dd HH:mm:ss.Sss", "yyyy/MM/dd HH:mm:ss.SSS xxx", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss XXx",
					"yyyyMMddHHmmss", "yyyy/MM/dd", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss XXx", "yyyyMMdd",
					"MM/dd/yyyyMM/ddlyyyy HH:mm:ss", "MM-dd-yyyy", "MM-dd-yyyy HH:mm:ss", "MM/dd/yy", "MM-dd-yy", "dd/MM/yyyy",
					"dd-MM-yyyy", "xy-MM.-dd", "HH:mm:ss.SSSXXX", "wy-MIM-da HH:mm:ss.sss"
				],
				replaceAllMask: ["yyyy/MM/dd HH:mm:ss.Sss", "yyyy/MM/dd HH:mm:ss.SSS xxx", "yyyy/MM/dd HH:mm:ss",
					"yyyy/MM/dd HH:mm:ss XXx",
					"yyyyMMddHHmmss", "yyyy/MM/dd", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss XXx", "yyyyMMdd",
					"MM/dd/yyyyMM/ddlyyyy HH:mm:ss", "MM-dd-yyyy", "MM-dd-yyyy HH:mm:ss", "MM/dd/yy", "MM-dd-yy", "dd/MM/yyyy",
					"dd-MM-yyyy", "xy-MM.-dd", "HH:mm:ss.SSSXXX", "wy-MIM-da HH:mm:ss.sss"
				],
				valuemask: ["yyyy/MM/dd HH:mm:ss.Sss", "yyyy/MM/dd HH:mm:ss.SSS xxx", "yyyy/MM/dd HH:mm:ss",
					"yyyy/MM/dd HH:mm:ss XXx",
					"yyyyMMddHHmmss", "yyyy/MM/dd", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss XXx", "yyyyMMdd",
					"MM/dd/yyyyMM/ddlyyyy HH:mm:ss", "MM-dd-yyyy", "MM-dd-yyyy HH:mm:ss", "MM/dd/yy", "MM-dd-yy", "dd/MM/yyyy",
					"dd-MM-yyyy", "xy-MM.-dd", "HH:mm:ss.SSSXXX", "wy-MIM-da HH:mm:ss.sss"
				],
				set_empty_string: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				set_type_empty_string: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				rules: {
					name: [{
						required: true,
						message: '作业名称不能为空',
						trigger: 'blur'
					}],
				},
			}
		},
		mounted() {
			this.init();
		},
		methods: {

			changefield(val) {

				if (this.step.selectFields == false && this.step.selectValuesType == false) {
					this.replace = false;
					this.setString = false;
					this.msak = false;
					this.tablevalue = true;
					this.tablefield = true;
					this.get = true;
				} else if ((this.step.selectFields == true && val == "0") || (this.step.selectFields == true && val == undefined)) {
					console.log(222);
					this.replace = true;
					this.setString = true;
					this.msak = true;
					this.tablevalue = true;
					this.step.selectValuesType = false;
					this.tablefield = false;
					this.get = false;
				} else if (this.step.selectValuesType == true && val == "1" || (this.step.selectValuesType == true && val ==
						undefined)) {
					console.log(111);
					this.replace = true;
					this.setString = true;
					this.msak = true;
					this.tablevalue = false;
					this.step.selectFields = false;
					this.tablefield = true;
					this.get = true;
				}

				console.log(this.step.selectFields);
				console.log(this.step.selectValuesType);
			},

			getDialog() {
				console.log(1);
				this.step.fields.field = [];
				this.lastStepFields = this.step['outFields'];
				console.log(this.step['outFields']);
				console.log(this.lastStepFields);
				this.lastStepFields.forEach(item => {
					this.step.fields.field.push({
						name: item.name,
					})
				});
			},

			addRowvalue() {
				this.step.valuetypes.valuetype.push({
					name: "",
					value: "",
					mask: "",
					set_type_empty_string: "",
				})
			},
			deleteRowvalue(index) {
				this.step.valuetypes.valuetype.splice(index, 1); //删掉该行
			},
			addRowfield() {
				this.step.fields.field.push({
					name: "",
					value: "",
					mask: "",
					set_empty_string: "",
				})
			},
			deleteRowfield(index) {
				this.step.fields.field.splice(index, 1); //删掉该行
			},
			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},

			init() {
				console.log(this.step);
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
				console.log(this.step['outFields']);

				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				console.log(JSON.stringify(obj));
				if (obj.initValid == undefined) {
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.name.push({
								name: item.name,
							})
						});
					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.name.push({
							name: item.name,
						})
					});
					this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
					this.step.valuetypes.valuetype = JSON.parse(JSON.stringify(obj.valuetypes.valuetype));
					console.log(JSON.stringify(obj));
					// this.step = obj;
				}
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
					this.step.selectFields = curstep.selectFields;
					this.step.selectValuesType = curstep.selectValuesType;
					this.step.replaceAllByValue = curstep.replaceAllByValue;
					this.step.setEmptyStringAll = curstep.setEmptyStringAll;
					this.step.mask = curstep.mask;
				}
				for (let key in this.step) {
					if (this.step[key] == 'Y') {
						this.step[key] = true;
					} else if (this.step[key] == 'N') {
						this.step[key] = false;
					}
				}
				console.log("1111", this.step);
				this.changefield();
				console.log(this.step['outFields']);
			},

			submit() {
				this.step.initFlag = true;
				this.step.initValid = false;
				console.info("最终字段", this.step);
				let step = Object.assign({}, this.step);
				if (this.step.selectFields == true) {
					step.selectFields = 'Y'
				} else {
					step.selectFields = 'N'
				}
				if (this.step.selectValuesType == true) {
					step.selectValuesType = 'Y'
				} else {
					step.selectValuesType = 'N'
				}
				if (this.step.setEmptyStringAll == true) {
					step.setEmptyStringAll = 'Y'
				} else {
					step.setEmptyStringAll = 'N'
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
				//新增字段

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
