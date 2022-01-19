<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%" clearable></el-input>
				</el-form-item>
			</el-form>
			<el-tabs>
				<el-tab-pane label="设置">
					<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
						<el-divider content-position="left">步骤设置</el-divider>
						<el-form-item label="要匹配的字段">
							<el-select v-model="step.matcher" size="mini" clearable filterable style="width: 65%">
								<el-option v-for="item in matchers" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="结果字段名">
							<el-input v-model="step.resultfieldname" style="width: 65%" size="mini" clearable></el-input>
						</el-form-item>
						<el-form-item>
							<el-checkbox label="为每个捕获组(capture group)创建一个字段" v-model="step.allowcapturegroups" @change="changeDis"></el-checkbox>
							<el-checkbox label="替换以前的字段" v-model="step.replacefields" :disabled="dis"></el-checkbox>
						</el-form-item>
						<el-form-item>
							<el-checkbox label="变量替换" v-model="step.usevar"></el-checkbox>
							<el-button @click="Test" size="mini" style="margin-left: 25%;"> 测试正则表达式</el-button>
						</el-form-item>
						<el-form-item label="正则表达式：">
							<el-input style="width: 65%" type="textarea" v-model="step.script" :autosize="{ minRows: 2}">
							</el-input>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<el-tab-pane label="内容">
					<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
						<el-divider content-position="left">Regex Settings</el-divider>
						<el-form-item>
							<el-checkbox label="正规分解(Canonical Decomposition)匹配" v-model="step.canoneq"></el-checkbox>
							<el-checkbox label="忽略大小写" v-model="step.caseinsensitive"></el-checkbox>
						</el-form-item>
						<el-form-item>
							<el-checkbox label="在表达式中允许有空格和注释" v-model="step.comment"></el-checkbox>
							<el-checkbox label="点子符(.)全部匹配模式" v-model="step.dotall"></el-checkbox>
						</el-form-item>
						<el-form-item>
							<el-checkbox label="启用多行模式" v-model="step.multiline"></el-checkbox>
							<el-checkbox label="Unicode忽略大小写" v-model="step.unicode"></el-checkbox>
							<el-checkbox label="Unix行模式" v-model="step.unix"></el-checkbox>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<div>
					<span>捕获组字段：</span>
					<el-button style="float:right" type="primary" @click="addRow" size="mini" :disabled=" dis">新增
					</el-button>
				</div>
			</el-tabs>
			<el-table :data="step.fields.field" border height="300" :header-cell-style="{background:'#eef1f6'}">
				<el-table-column type="index" label="#" width="50"></el-table-column>
				<el-table-column label="新字段" prop="name">
					<template slot-scope="scope">
						<el-input v-model="scope.row.name" @blur="blurInput(scope.row,scope.$index)" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="类型" prop="type" width="150">
					<template slot-scope="scope">
						<el-select v-model="scope.row.type" size="mini" clearable :disabled="dis">
							<el-option v-for="item in type" :key="item" :label="item" :value="item"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="长度" prop="length">
					<template slot-scope="scope">
						<el-input v-model="scope.row.length" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="精度" prop="precision">
					<template slot-scope="scope">
						<el-input v-model="scope.row.precision" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="格式" prop="format">
					<template slot-scope="scope">
						<el-input v-model="scope.row.format" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="分组符号" prop="group" width="100">
					<template slot-scope="scope">
						<el-input v-model="scope.row.group" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="十进制" prop="decimal">
					<template slot-scope="scope">
						<el-input v-model="scope.row.decimal" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="货币" prop="currency">
					<template slot-scope="scope">
						<el-input v-model="scope.row.currency" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="如果...则置空" prop="nullif" width="150">
					<template slot-scope="scope">
						<el-input v-model="scope.row.nullif" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="Default" prop="ifnull" width="100">
					<template slot-scope="scope">
						<el-input v-model="scope.row.ifnull" size="mini" clearable :disabled="dis"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="删除空格" prop="trimtype" width="150">
					<template slot-scope="scope">
						<el-select v-model="scope.row.trimtype" size="mini" clearable :disabled="dis">
							<el-option v-for="item in trimtype" :key="item.key" :label="item.label" :value="item.key"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="text" @click="deleteRow(scope.$index)" :disabled="dis">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button type="primary" @click="submit" size="mini">确 定</el-button>
		</div>
		<!-- 正则测试 -->
		<el-dialog title="正则表达式测试" :visible.sync="regexEvalVisiable" class="dialog_temp other_dialog" :modal-append-to-body="false"
		 :append-to-body="true" width="50%">
			<!-- 子组件 -->
			<RegexEvals :scripts="scripts"></RegexEvals>
		</el-dialog>

	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store.js";
	import RegexEvals from "../../../common/TestRegex.vue"
	import {
		getChecktableName
	} from "../../../../common/common";
  import {compareFields} from "../../../../assets/common/tool";
	export default {
		components: {
			RegexEvals,
		},
		data() {
			return {
				dis:true,
				scripts: "",
				regexEvalVisiable: false,
				key: '', //插件对应的画布
				nodeData: {},
				matchers: [],
				step: {
					name: '正则表达式',
					type: "RegexEval",
					description: [],
					distribute: "Y",
					custom_distribution: [],
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					matcher: "",
					script: "",
					resultfieldname: "",
					usevar: false,
					allowcapturegroups: false,
					replacefields: true,
					canoneq: false,
					caseinsensitive: false,
					comment: false,
					dotall: false,
					multiline: false,
					unicode: false,
					unix: false,
					fields: {
						field:[]
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
				type: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Serializable", "Binary", "Timestamp"],
				trimtype: [{
					key: 'none',
					label: '不去掉空格'
				}, {
					key: 'left',
					label: '去掉左空格'
				}, {
					key: 'right',
					label: '去掉右空格'
				}, {
					key: 'both',
					label: '去掉左右两端空格'
				}],
				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						trigger: 'blur'
					}],
				},
			}
		},
		mounted() {
			this.init();
		},
		methods: {
			blurInput(row, index) {
				let args = JSON.parse(JSON.stringify(this.step.fields.field));
				args.splice(index, 1);
				getChecktableName(args, this.step.outFields, row, 'name');
				// let tname = store.getters.getChecktableName(args,this.step.outFields,row);
				this.$message({message:'该字段名已存在，重命名为'+row.name,
								type:'warning'});

			},
			Test() {
				this.regexEvalVisiable = true;
				console.log(11);
				this.scripts = this.step.script;
			},
			changeDis(){
				if(this.step.allowcapturegroups == true){
					this.dis = false;
				} else if(this.step.allowcapturegroups == false){
					this.dis = true;
				}
			},
			addRow() {
				this.step.fields.field.push({
					name: "",
					type: "String",
					format: "",
					group: "",
					decimal: "",
					length: "255",
					precision: "-1",
					nullif: "",
					ifnull: "",
					trimtype: "",
					currency: ""
				})
			},
			deleteRow(index) {
				this.step.fields.field.splice(index, 1); //删掉该行
			},
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
				compareFields(outFields)
        console.info("outFields",outFields);
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
						console.log(this.lastStepFields);
						this.matchers = [];
						this.lastStepFields.forEach(item => {
							this.matchers.push({
								name: item.name
							})
						});
						console.log(this.matchers);
					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					console.log(this.lastStepFields);
					this.matchers = [];
					this.lastStepFields.forEach(item => {
						this.matchers.push({
							name: item.name
						})
					});
					this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
					console.log(JSON.stringify(obj));
					// this.step = obj;
					console.log(this.matchers);
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
					this.step.matcher = curstep.matcher === undefined ? '' : curstep.matcher;
					this.step.script = curstep.script;
					this.step.resultfieldname = curstep.resultfieldname;
					this.step.usevar = curstep.usevar;
					this.step.allowcapturegroups = curstep.allowcapturegroups;
					this.step.replacefields = (curstep.replacefields === 'Y' || curstep.replacefields === undefined);
					this.step.canoneq = curstep.canoneq;
					this.step.caseinsensitive = curstep.caseinsensitive;
					this.step.comment = curstep.comment;
					this.step.dotall = curstep.dotall;
					this.step.multiline = curstep.multiline;
					this.step.unicode = curstep.unicode;
					this.step.unix = curstep.unix;

				}
				for (let key in this.step) {
					if (this.step[key] == 'Y') {
						this.step[key] = true;
					} else if (this.step[key] == 'N') {
						this.step[key] = false;
					}
				}
				this.changeDis();
				console.log("1111", this.step);
				console.log(this.step['outFields']);
			},
			submit() {
				this.step.initFlag = true;
				this.step.initValid = false;
				console.info("最终字段", this.step);
				let step = Object.assign({}, this.step);
				step.usevar = this.step.usevar ? 'Y' : 'N';
				step.allowcapturegroups = this.step.allowcapturegroups ? 'Y' : 'N';
				step.replacefields = this.step.replacefields ? 'Y' : 'N';
				step.canoneq = this.step.canoneq ? 'Y' : 'N';
				step.caseinsensitive = this.step.caseinsensitive ? 'Y' : 'N';
				step.comment = this.step.comment ? 'Y' : 'N';
				step.dotall = this.step.dotall ? 'Y' : 'N';
				step.multiline = this.step.multiline ? 'Y' : 'N';
				step.unicode = this.step.unicode ? 'Y' : 'N';
				step.unix = this.step.unix ? 'Y' : 'N';
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
				console.log(step);
				console.log(11111);
				//新增字段
				step.fields.field.forEach(item => {
					if (item.name !== name) {
						step.outFields.push({
							name: item.name,
							type: item.type,
							length: item.length,
							precision: item.precision,
							trim_type: "none",
							repeat: "N",
							format: "",
							currency: item.currency === '' ? '¥' : item.currency,
							decimal: item.decimal === '' ? "." : item.decimal,
							group: item.group === '' ? ',' : item.group
						})
					}
				});

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
