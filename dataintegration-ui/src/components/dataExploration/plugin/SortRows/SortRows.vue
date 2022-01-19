<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="stepfrom" :model="step" :rules="rules" size="mini" label-width="30%">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;" size="mini"></el-input>
				</el-form-item>
				<el-form-item label="排序目录" prop="directory">
					<el-input v-model="step.directory" style="width: 65%;" size="mini"></el-input>
				</el-form-item>
				<el-form-item label="临时文件前缀" prop="prefix">
					<el-input v-model="step.prefix" style="width: 65%" size="mini"></el-input>
				</el-form-item>
				<el-form-item label="排序缓存大小(内存里存放的记录数)" prop="sort_size">
					<el-input v-model="step.sort_size" style="width: 65%" size="mini"></el-input>
				</el-form-item>
				<el-form-item label="未使用内存限值(%)" prop="free_memory">
					<el-input v-model="step.free_memory" style="width: 65%" size="mini"></el-input>
				</el-form-item>
				<el-form-item>
					<el-checkbox label="临时压缩文件?" v-model="step.compress"></el-checkbox>
					<el-checkbox label="仅仅传递非重复的记录?(仅仅校验关键字)" v-model="step.unique_rows"></el-checkbox>
				</el-form-item>
			</el-form>
			<div>
				<el-button style="float:right;margin-bottom: 5px;margin-right: 5px" type="primary" @click="addRow" size="mini">新增
				</el-button>
			</div>
			<el-table :data="step.fields.field" border height="330" :header-cell-style="{background:'#eef1f6'}">
				<el-table-column type="index" label="#" width="50"></el-table-column>
				<el-table-column prop="name" label="字段名称" width="100">
					<template slot-scope="scope">
						<el-select v-model="scope.row.name" size="mini" style="width: 100%;" clearable filterable allow-create>
							<el-option v-for="item in tablename" :key="item.name" :label="item.name" :value="item.name">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="ascending" label="升序">
					<template slot-scope="scope">
						<el-select v-model="scope.row.ascending" size="mini" style="width: 100%;" clearable>
							<el-option v-for="item in ascending" :key="item.key" :label="item.label" :value="item.key">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="case_sensitive" label="大小写敏感" width="140">
					<template slot-scope="scope">
						<el-select v-model="scope.row.case_sensitive" size="mini" style="width: 100%;" clearable>
							<el-option v-for="item in case_sensitive" :key="item.key" :label="item.label" :value="item.key">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="collator_enabled" label="根据当前区域设置排序?" width="260">
					<template slot-scope="scope">
						<el-select v-model="scope.row.collator_enabled" size="mini" style="width: 100%;" clearable>
							<el-option v-for="item in collator_enabled" :key="item.key" :label="item.label" :value="item.key">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="collator_strength" label="排序优先级" width="160">
					<template slot-scope="scope">
						<el-select v-model="scope.row.collator_strength" size="mini" style="width: 100%;" clearable>
							<el-option v-for="item in collator_strength" :key="item" :label="item" :value="item">
							</el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column prop="presorted" label="预先分类?" width="120">
					<template slot-scope="scope">
						<el-select v-model="scope.row.presorted" size="mini" style="width: 100%;" clearable>
							<el-option v-for="item in presorted" :key="item.key" :label="item.label" :value="item.key">
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


	export default {
		data() {
			return {
				key: '', //插件对应的画布
				nodeData: {},
				fileList: [],
				tablename: [],
				ascending: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				collator_enabled: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				case_sensitive: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				presorted: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				collator_strength: ["0", "1", "2", "3", "4"],
				step: {
					name: "排序记录",
					type: "SortRows",
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

					ftp_username: "",
					ftp_password: "",
					directory: "%%java.io.tmpdir%%",
					prefix: "out",
					sort_size: 1000000,
					free_memory: " ",
					compress: false,
					compress_variable: [],
					unique_rows: false,
					attributes: [],
					cluster_schema: [],
					remotesteps: {
						input: "\n      ",
						output: "\n      "
					},
					GUI: {
						xloc: 320,
						yloc: 64,
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
				// this.curPage = parseInt(this.step.fields.field.length / this.pageSize + 1);
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
					obj.initValid = false;
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.tablename.push({
								name: item.name,

							})
						});
					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.tablename.push({
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
					//console.log(this.laststep.fields.field);
					this.step.directory == undefined ? '%%java.io.tmpdir%%' : curstep.directory;
					this.step.prefix == undefined ? 'out' : curstep.prefix;
					this.step.sort_size = curstep.sort_size == undefined ? '1000000' : curstep.sort_size;
					this.step.free_memory = curstep.free_memory;
					this.step.compress = curstep.compress;
					this.step.unique_rows = curstep.unique_rows;
				}
			},

			submit(stepfrom) {
				this.step.initFlag = true;
				this.step.initValid = false;
				let step = Object.assign({}, this.step);
				if (this.step.compress == true) {
					step.compress = 'Y';
				} else {
					step.compress = 'N';
				}
				if (this.step.unique_rows == true) {
					step.unique_rows = 'Y';
				} else {
					step.unique_rows = 'N';
				}
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

						//新增字段
						// step.fields.field.forEach(item => {
						//   step.outFields.forEach((sub,index)=>{
						//     if(sub.name === item.name){
						//       item.name = item.name+'_'+1;
            //     }
            //   })
						// 	step.outFields.push({
						// 		name: item.name,
						// 		type: item.type,
						// 		length: -1,
						// 		precision: -1,
						// 		trim_type: "none",
						// 		repeat: "N",
						// 		format: "",
						// 		currency: item.currency === '' ? '¥' : item.currency,
						// 		decimal: item.decimal === '' ? "." : item.decimal,
						// 		group: item.group === '' ? ',' : item.group
						// 	})
						// })

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

		}
	}
</script>

<style>
	.el-form-item--mini.el-form-item {
		margin-bottom: 10px;
	}

	input[type="file"] {
		display: none;
	}
</style>
