<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" size="mini" style="width: 65%;"></el-input>
				</el-form-item>
				<el-form-item>
					<el-checkbox label="在常量中使用变量" v-model="step.usevar"></el-checkbox>
				</el-form-item>
			</el-form>
			<div>
				<el-button style="float:right;margin-bottom: 5px;margin-right: 5px" type="primary" @click="addRow" size="mini">新增
				</el-button>
			</div>
			<el-table :data="step.fields.field" border height="330" :header-cell-style="{background:'#eef1f6'}">
				<el-table-column type="index" label="#" width="50"></el-table-column>
				<el-table-column label="字段" prop="name">
					<template slot-scope="scope">
						<el-select size="mini" v-model="scope.row.name">
							<el-option v-for="item in name" :key="item.name" :label="item.name" :value="item.name"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="值替换" prop="value">
					<template slot-scope="scope">
						<el-input size="mini" v-model="scope.row.value"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="转换掩码(对日期类型)" width="240" prop="mask">
					<template slot-scope="scope">
						<el-select size="mini" v-model="scope.row.mask">
							<el-option v-for="item in mask" :key="item" :label="item" :value="item"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="设为空串?" prop="set_empty_string">
					<template slot-scope="scope">
							<el-select size="mini" v-model="scope.row.set_empty_string">
								<el-option v-for="item in set_empty_string" :key="item.key" :label="item.label" :value="item.key"></el-option>
							</el-select>
						</template>
					</el-table-column>
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer" align="right">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button @click="getDialog" size="mini">获取字段</el-button>
			<el-button type="primary" @click="submit" size="mini">确 定</el-button>
		</div>
	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from '../../../../vuex/store.js';
	
	export default{
		data(){
			return{
				key:'',
				nodeData:{},
				name:[],
				mask:["yyyy/MM/dd HH:mm:ss.Sss", "yyyy/MM/dd HH:mm:ss.SSS xxx", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss XXx",
					"yyyyMMddHHmmss", "yyyy/MM/dd", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss XXx", "yyyyMMdd",
					"MM/dd/yyyyMM/ddlyyyy HH:mm:ss", "MM-dd-yyyy", "MM-dd-yyyy HH:mm:ss", "MM/dd/yy", "MM-dd-yy", "dd/MM/yyyy",
					"dd-MM-yyyy", "xy-MM.-dd", "HH:mm:ss.SSSXXX", "wy-MIM-da HH:mm:ss.sss"],
				set_empty_string:[{
						key:'Y',
						label:'是'
					},{
						key:'N',
						label:'否'
					}],
				step: {
					name: "将字段值设置为常量",
					type: "SetValueConstant",
					description: [],
					distribute: "Y",
					custom_distribution: [],
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					usevar: false,
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
						xloc: "368",
						yloc: "64",
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
			this.init()
		},
		methods:{
			
			//新增，删除
			addRow() {
				this.step.fields.field.push({
					name: "",
					value: "",
					mask: "",
					set_empty_string: ""
				})
			},
			deleteRow(index) { //移除一行
				this.step.fields.field.splice(index, 1); //删掉该行
			},
			
			//获取字段
			getDialog(){
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
			
			closeDialog(){
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},
			init(){
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
					this.step.usevar =  curstep.usevar ;
				}
				for (let key in this.step) {
					if (this.step[key] == 'Y') {
						this.step[key] = true;
					} else if (this.step[key] == 'N') {
						this.step[key] = false;
					}
				}
				console.log("1111", this.step);
				console.log(this.step['outFields']);
			},
			submit(){
				this.step.initFlag = true;
				this.step.initValid = false;
				console.info("最终字段", this.step);
				let step = Object.assign({}, this.step);
				if (this.step.usevar == true) {
					step.usevar = 'Y'
				} else {
					step.usevar = 'N'
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
				console.log(step);
				console.log(11111);
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
