<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="step" class="title_div" :model="step" :rules="rules" label-width="30%" size="mini">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" size="mini" style="width: 65%;"></el-input>
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
						<el-input v-model="scope.row.name" size="mini" clearable></el-input>
					</template>
				</el-table-column>
				<el-table-column prop="type" label="类型">
					<template slot-scope="scope">
						<el-select v-model="scope.row.type" size="mini" style="width: 100%;" @change="formatData(scope.row)">
							<el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="format" label="格式">
					<template slot-scope="scope">
						<el-select v-model="scope.row.format" size="mini" style="width: 100%;" clearable>
							<el-option v-for="item in formats" :key="item" :label="item" :value="item">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="length" label="长度">
					<template slot-scope="scope">
						<el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.length">
						</el-input>
					</template>
				</el-table-column>
				<el-table-column prop="precision" label="精确">
					<template slot-scope="scope">
						<el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.precision">
						</el-input>
					</template>
				</el-table-column>
				<el-table-column prop="currency" label="当前的">
					<template slot-scope="scope">
						<el-input size="mini" style="width: 100%;" v-model="scope.row.currency">
						</el-input>
					</template>
				</el-table-column>
				<el-table-column prop="decimal" label="10进制的" width="120">
					<template slot-scope="scope">
						<el-input size="mini" style="width: 100%;" v-model="scope.row.decimal">
						</el-input>
					</template>
				</el-table-column>
				<el-table-column prop="group" label="组">
					<template slot-scope="scope">
						<el-input size="mini" style="width: 100%;" v-model="scope.row.group">
						</el-input>
					</template>
				</el-table-column>
				<el-table-column prop="nullif" label="值">
					<template slot-scope="scope">
						<el-input size="mini" style="width: 100%;" v-model="scope.row.nullif">
						</el-input>
					</template>
				</el-table-column>
				<el-table-column prop="set_empty_string" label="设为空串?" width="120">
					<template slot-scope="scope">
						<el-select v-model="scope.row.set_empty_string" size="mini" style="width: 100%;" clearable>
							<el-option v-for="item in set_empty_string" :key="item.key" :label="item.label" :value="item.key">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!--<el-pagination style="float: right" class="right" @current-change="selectedPage" :current-page.sync="curPage"
				 @size-change="selectedPage" :page-size.sync="pageSize" :page-sizes="[10, 50, 150]" layout="total, sizes, prev, pager, next"
				 :total="total">
				</el-pagination>-->
		</div>
		<div slot="footer" class="dialog-footer plugin_footer" align="right">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button type="primary" @click="submit" size="mini">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from '../../../../vuex/store.js';
	import {
		getChecktableName
	} from "../../../../common/common";
	export default {
		data() {
			return {
				key: '', //插件对应的画布
				nodeData: {},
				step: {
					name: "增加常量",
					type: "Constant",
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
						xloc: 200,
						yloc: 100,
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

				set_empty_string: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				fieldTypes: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"], //字段类型
				formats: ["General", "0", "0.00", "#,##0", "#,##0.00", "\"$\"#,##0_);(\"$\"#,##0)",
					"\"$\"#,##0_);[Red](\"$\"#,##0)", "\"$\"#,##0.00_);(\"$\"#,##0.00)", "\"$\"#,##0.00_);[Red](\"$\"#,##0.00)",
					"0%", "0.00%", "0.00E+00", "# ?/?", "# ??/??", "m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM",
					"h:mm:ss AM/PM", "h:mm", "h:mm:ss", "m/d/yy h:mm", "reserved-0x17", "reserved-0x18", "reserved-0x19",
					"reserved-0x1A", "reserved-0x1B", "reserved-0x1C", "reserved-0x1D", "reserved-0x1E", "reserved-0x1F",
					"reserved-0x20", "reserved-0x21", "reserved-0x22", "reserved-0x23", "reserved-0x24", "#,##0_);(#,##0)",
					"#,##0_);[Red](#,##0)", "#,##0.00_);(#,##0.00)", "#,##0.00_);[Red](#,##0.00)",
					"_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)", "_(\"$\"* #,##0_);_(\"$\"* (#,##0);_(\"$\"* \"-\"_);_(@_)",
					"_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)",
					"_(\"$\"* #,##0.00_);_(\"$\"* (#,##0.00);_(\"$\"* \"-\"??_);_(@_)", "mm:ss", "[h]:mm:ss", "mm:ss.0",
					"##0.0E+0", "@"
				], //字段格式
				formatNumber: ["0", "0.00", "#,##0", "#,##0.00", "\"$\"#,##0_);(\"$\"#,##0)",
					"\"$\"#,##0_);[Red](\"$\"#,##0)", "\"$\"#,##0.00_);(\"$\"#,##0.00)", "\"$\"#,##0.00_);[Red](\"$\"#,##0.00)",
					"0%", "0.00%", "0.00E+00", "# ?/?", "# ??/??"
				],
				formatDate: ["m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM", "h:mm:ss AM/PM", "h:mm", "h:mm:ss",
					"m/d/yy h:mm",
					"mm:ss", "[h]:mm:ss", "mm:ss.0", "yyyy-mm-dd hh:mm:ss", "yyyy/mm/dd", "yyyy-MM-dd", "yyyy/mm/dd hh/mm/ss"
				],
				trimType: ["none", "left", "right", "both"],
			}
		},
		mounted() {
			this.init();
		},

		methods: {

			blurInput(row,index) {
				let args=JSON.parse(JSON.stringify(this.step.fields.field));
				args.splice(index,1);
				getChecktableName(args,this.step.outFields,row,'name');
				this.$message({message:'该字段名已存在，重命名为'+row.name,
								type:'warning'});
			},

			formatData(row) {
				if (row.type === 'Number') {
					this.formats = this.formatNumber;
					row.length = 38;

				} else if (row.type === 'Date') {
					this.formats = this.formatDate;
					row.length = -1;

				} else {
					row.length = -1;

				}
			},


			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},

			addRow(event) { //新增一行
				this.step.fields.field.push({
					name: '',
					type: 'String',
					format: '',
					length: 255,
					precision: -1,
					group: '',
					currency: '',
					set_empty_string: '',
					nullif: '',
					decimal: '',
				});
			},

			deleteRow(index) { //移除一行
				this.step.fields.field.splice(index, 1); //删掉该行
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
				console.log(JSON.stringify(obj) + 1111111);
				if (obj.initValid == undefined) {
					obj.initValid = false;
					if (this.laststep !== undefined) {
						this.lastStepField = JSON.parse(JSON.stringify(outFields));
					}
				} else {
					this.lastStepField = JSON.parse(JSON.stringify(outFields));
					this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
					console.log(JSON.stringify(obj));
					console.log(this.step.fields.field);
				}


			},



			submit() {
				this.step.initFlag = true;
				console.info("最终字段", this.step);
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
