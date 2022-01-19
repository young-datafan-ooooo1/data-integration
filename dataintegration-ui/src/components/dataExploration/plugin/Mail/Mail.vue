<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%" clearable></el-input>
				</el-form-item>
			</el-form>
			<el-tabs>
				<el-tab-pane label="地址">
					<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
						<el-divider content-position="left">收件人</el-divider>
						<el-form-item label="收件人地址" prop="destination">
							<el-select v-model="step.destination" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in destination" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="抄送" prop="destinationCc">
							<el-select v-model="step.destinationCc" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in destinationCc" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="暗送" prop="destinationBCc">
							<el-select v-model="step.destinationBCc" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in destinationBCc" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-divider content-position="left">发件人</el-divider>
						<el-form-item label="发件人姓名" prop="replytoname">
							<el-select v-model="step.replytoname" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in replytoname" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="发件人地址" prop="replyto">
							<el-select v-model="step.replyto" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in replyto" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="回复" prop="replyToAddresses">
							<el-select v-model="step.replyToAddresses" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in replyToAddresses" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="联系人" prop="contact_person">
							<el-select v-model="step.contact_person" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in contact_person" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="联系电话" prop="contact_phone">
							<el-select v-model="step.contact_phone" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in contact_phone" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<el-tab-pane label="服务器">
					<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
						<el-divider content-position="left">SMTP服务器</el-divider>
						<el-form-item label="SMTP服务器" prop="server">
							<el-select v-model="step.server" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in server" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="端口" prop="port">
							<el-select v-model="step.port" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in port" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-divider content-position="left">验证</el-divider>
						<el-form-item label="用户验证" prop="use_auth">
							<el-checkbox v-model="step.use_auth" @change="change_use_auth"></el-checkbox>
						</el-form-item>
						<el-form-item label="用户名" prop="auth_user">
							<el-select v-model="step.auth_user" style="width: 65%;" filterable allow-create clearable :disabled="authu">
								<el-option v-for="item in auth_user" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="密码" prop="auth_password">
							<el-select v-model="step.auth_password" style="width: 65%;" filterable allow-create clearable :disabled="password">
								<el-option v-for="item in auth_password" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="使用安全认证" prop="use_secure_auth">
							<el-checkbox v-model="step.use_secure_auth" :disabled="yshi" @change="sy"></el-checkbox>
						</el-form-item>
						<el-form-item label="安全连接类型" prop="secureconnectiontype">
							<el-select v-model="step.secureconnectiontype" style="width: 65%;" filterable allow-create clearable :disabled="secure">
								<el-option v-for="item in secureconnectiontype" :key="item" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<el-tab-pane label="邮件正文">
					<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
						<el-divider content-position="left">消息设置</el-divider>
						<el-form-item>
							<el-checkbox label="在邮件中包含日期" v-model="step.include_date"></el-checkbox>
							<el-checkbox label="只发送邮件正文" v-model="step.only_comment"></el-checkbox>
						</el-form-item>
						<el-form-item label="编码" prop="encoding">
							<el-select v-model="step.encoding" style="width: 65%;" filterable allow-create clearable :disabled="html">
								<el-option v-for="item in encoding" :key="item" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item>
							<el-checkbox label="邮件正文使用HTML格" v-model="step.use_HTML" @change="geshi"></el-checkbox>
							<el-checkbox label="优先级设置" v-model="step.use_Priority" @change="youxian"></el-checkbox>
						</el-form-item>
						<el-form-item label="优先级" prop="priority">
							<el-select v-model="step.priority" style="width: 65%;" :disabled="pr" filterable allow-create clearable>
								<el-option v-for="item in priority" :key="item.key" :label="item.label" :value="item.key"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="重要性" prop="importance">
							<el-select v-model="step.importance" style="width: 65%;" :disabled="im" filterable allow-create clearable>
								<el-option v-for="item in importance" :key="item.key" :label="item.label" :value="item.key"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="敏感" prop="sensitivity">
							<el-select v-model="step.sensitivity" style="width: 65%;" :disabled="se" filterable allow-create clearable>
								<el-option v-for="item in sensitivity" :key="item.key" :label="item.label" :value="item.key"></el-option>
							</el-select>
						</el-form-item>
						<el-divider content-position="left">消息</el-divider>
						<el-form-item label="主题" prop="subject">
							<el-select v-model="step.subject" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in subject" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="邮件正文" prop="comment">
							<el-select v-model="step.comment" style="width: 65%;" filterable allow-create clearable>
								<el-option v-for="item in comment" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<el-tab-pane label="附件">
					<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
						<el-divider content-position="left">附加内容文件</el-divider>
						<el-form-item prop="attachContentFromField">
							<el-checkbox label="附加内容文件" v-model="step.attachContentFromField" @change="fujia"></el-checkbox>
						</el-form-item>
						<el-form-item label="内容字段" prop="attachContentField">
							<el-select v-model="step.attachContentField" style="width: 65%;" :disabled="nz" filterable allow-create
							 clearable>
								<el-option v-for="item in attachContentField" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="文件名字段名" prop="attachContentFileNameField">
							<el-select v-model="step.attachContentFileNameField" style="width: 65%;" :disabled="wz" filterable allow-create
							 clearable>
								<el-option v-for="item in attachContentFileNameField" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-divider content-position="left">附件文件名</el-divider>
						<el-form-item>
							<el-checkbox label="动态文件名?" v-model="step.isFilenameDynamic" prop="isFilenameDynamic" :disabled="dt" @change="dongtai"></el-checkbox>
						</el-form-item>
						<el-form-item label="文件名字段" prop="dynamicFieldname">
							<el-select v-model="step.dynamicFieldname" style="width: 65%;" filterable allow-create clearable :disabled="mz">
								<el-option v-for="item in dynamicFieldname" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="通配符字段" prop="dynamicWildcard">
							<el-select v-model="step.dynamicWildcard" style="width: 65%;" filterable allow-create clearable :disabled="tp">
								<el-option v-for="item in dynamicWildcard" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="文件名/目录" prop="sourcefilefoldername">
							<el-autocomplete class="inline-input" v-model="step.sourcefilefoldername" :fetch-suggestions="querySearchfile"
							 placeholder="请输入文件名称搜索文件" :trigger-on-focus="false" @select="handleSelectfile" @blur="blurInputfile" style="width:65%"
							 :disabled="fuwen">
								<el-button slot="append" size="mini" type="primary" @click="changeFilesource('file')" :disabled="fuwen">文件</el-button>
								<el-button slot="append" size="mini" type="primary" @click="changeFilesource('folder')" :disabled="fuwen">目录</el-button>
							</el-autocomplete>
						</el-form-item>
						<el-form-item>
							<el-checkbox prop="include_subfolders" label="包括子目录" v-model="step.include_subfolders" :disabled="zimu"></el-checkbox>
						</el-form-item>
						<el-form-item label="通配符" prop="sourcewildcard">
							<el-input style="width: 65%;" v-model="step.sourcewildcard" clearable :disabled="tpfu"></el-input>
						</el-form-item>
						<el-divider content-position="left">Zip文件</el-divider>
						<el-form-item>
							<el-checkbox label="Zip文件" v-model="step.zip_files" :disabled="zwen" @change="zipWen"></el-checkbox>
							<el-checkbox label="动态zip文件名?" v-model="step.zipFilenameDynamic" :disabled="zwenm" @change="zLast"></el-checkbox>
						</el-form-item>
						<el-form-item label="Zip文件名字段" prop="dynamicZipFilename">
							<el-select v-model="step.dynamicZipFilename" style="width: 65%;" filterable allow-create clearable :disabled="zwz">
								<el-option v-for="item in dynamicZipFilename" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="Zip文件名" prop="zip_name">
							<el-input style="width: 65%;" v-model="step.zip_name" clearable :disabled="zwjm"></el-input>
						</el-form-item>
						<el-form-item label="如果附件大于...则进行压缩" prop="zip_limit_size">
							<el-input v-model="step.zip_limit_size" style="width: 65%;" clearable :disabled="fjdy"></el-input>
						</el-form-item>
					</el-form>
				</el-tab-pane>
				<el-tab-pane label="内嵌图片">
					<el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
						<el-form-item label="文件名" prop="image_name">
							<el-autocomplete class="inline-input" v-model="step.showFileName" :fetch-suggestions="querySearch" placeholder="请输入文件名称搜索文件"
							 :trigger-on-focus="false" @select="handleSelect" @blur="blurInput" style="width:65%" :disabled="wen">
								<el-button slot="append" size="mini" type="primary" @click="changeFiletu" :disabled="wen">本地文件</el-button>
							</el-autocomplete>
						</el-form-item>
						<el-form-item label="内容ID" prop="content_id">
							<el-input style="width: 65%;" clearable :disabled="ro"></el-input>
						</el-form-item>
					</el-form>
					<el-table :data="step.embeddedimages.embeddedimage" border height="330" :header-cell-style="{background:'#eef1f6'}">
						<el-table-column type="index" label="#" width="50"></el-table-column>
						<el-table-column prop="image_name" label="图片">
							<template slot-scope="scope">
								<el-input v-model="scope.row.image_name" size="mini" clearable :disabled="tu"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="content_id" label="内容ID">
							<template slot-scope="scope">
								<el-input v-model="scope.row.content_id" size="mini" clearable :disabled="ne"></el-input>
							</template>
						</el-table-column>
						<el-table-column label="操作" width="150">
							<template slot-scope="scope">
								<el-button size="mini" type="text" @click="deleteFile(scope.$index,step.embeddedimages.embeddedimage)">移除</el-button>
							</template>
						</el-table-column>
					</el-table>
				</el-tab-pane>
			</el-tabs>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer">
			<el-button @click="closeDialog" size="mini">取 消</el-button>
			<el-button type="primary" @click="submit" size="mini">确 定</el-button>
		</div>

		<!-- 图片文件 -->
		<el-dialog title="本地文件上传" :visible.sync="selectFileVisiabletufile" class="dialog_temp other_dialog"
		 :modal-append-to-body="false" :append-to-body="true" width="50%">
			<!-- 子组件 -->
			<fileData :params="queryParams" :selec-data="selectData" :fileType="fileTypetu" createChannel="UPLOAD" fileFolder="0"
			 ref="pcfileData"></fileData>
		</el-dialog>

		<!-- 文件 -->
		<el-dialog title="本地文件上传" :visible.sync="selectFileVisiablefile" class="dialog_temp other_dialog"
		 :modal-append-to-body="false" :append-to-body="true" width="50%">
			<!-- 子组件 -->
			<fileData :params="queryParams" :selec-data="selectData" :fileType="fileType" createChannel="UPLOAD" fileFolder="0"
			 ref="fileData"></fileData>
		</el-dialog>

		<!-- 目录 -->
		<el-dialog title="选择文件" :visible.sync="selectFileVisiablefolder" class="dialog_temp other_dialog"
		 :modal-append-to-body='false' :append-to-body="true" width="50%">
			<!-- 子组件 -->
			<fileData :selec-data="selectData" :fileType="fileType" fileFolder="1" createChannel="OUTPUT" ref="folderData"></fileData>
		</el-dialog>

	</div>
</template>

<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store.js";
	import fileData from '../../../common/UploadFile.vue';

	import {

		selectFileListByType,

	} from "../../../../api/api.js";
	export default {
		props: {
			selectData: Object,
		},
		components: {
			fileData,
		},
		data() {
			return {
				nz: true,
				wz: true,
				dt: false,
				mz: true,
				tp: true,
				fuwen: false,
				zimu: false,
				tpfu: false,
				zwen: false,
				zwenm: true,
				zwz: true,
				zwjm: true,
				fjdy: true,
				inputwen: true,
				wen: true,
				ro: true,
				tu: true,
				ne: true,
				html: true,
				yshi: true,
				authu: true,
				secure: true,
				password: true,
				pr: true,
				im: true,
				se: true,
				selectFileVisiabletufile: false,
				selectFileVisiablefile: false,
				selectFileVisiablefolder: false,
				key: '', //插件对应的画布
				nodeData: {},
				step: {
					showFileName: '',
					sourcefilefoldername: '',
					name: "发送邮件",
					type: "Mail",
					distribute: "Y",
					copies: "1",
					partitioning: {
						method: "none"
					},
					destination: "",
					destinationCc: "",
					destinationBCc: "",
					replytoname: "",
					replyto: "",
					replyToAddresses: "",
					contact_person: "",
					contact_phone: "",
					server: "",
					port: "",
					isFilenameDynamic: "",
					sourcewildcard: "",
					use_auth: false,
					auth_user: "",
					auth_password: "",
					use_secure_auth: "",
					secureconnectiontype: "SSL",
					include_date: false,
					only_comment: false,
					use_HTML: false,
					attachContentFromField: false,
					encoding: "UTF-8",
					use_Priority: false,
					priority: "normal",
					importance: "normal",
					sensitivity: "normal",
					subject: "",
					comment: "",
					attachContentField: "",
					attachContentFileNameField: "",
					dynamicFieldname: "",
					dynamicWildcard: "",
					dynamicZipFilename: "",
					embeddedimages: {
						embeddedimage: []
					},
				},
				FileSelect: [],
				destination: [],
				destinationCc: [],
				destinationBCc: [],
				replytoname: [],
				replyto: [],
				replyToAddresses: [],
				contact_person: [],
				contact_phone: [],
				server: [],
				port: [],
				auth_user: [],
				auth_password: [],
				secureconnectiontype: ["SSL", "TLS"],
				subject: [],
				comment: [],
				encoding: ["Big5", "Big5-HKSCS", "CESU-8", "EUC-JP", "EUC-KR", "GB18030", "GB2312", "GBK", "IBM-Thai",
					"IBM00858", "IBM01140", "IBM01141", "IBM01142", "IBM01143", "IBM01144", "IBM01145", "IBM01146", "IBM01147",
					"IBM01148", "IBM01149", "IBM037", "IBM1026", "IBM1047", "IBM273", "IBM277", "IBM278", "IBM280", "IBM284",
					"IBM285", "IBM290", "IBM297", "IBM420", "IBM424", "IBM437", "IBM500", "IBM775", "IBM850", "IBM852",
					"IBM855", "IBM857", "IBM860", "IBM861", "IBM862", "IBM863", "IBM864", "IBM865", "IBM866", "IBM868",
					"IBM869", "IBM870", "IBM871", "IBM918", "ISO-2022-CN", "ISO-2022-JP", "ISO-2022-JP-2", "ISO-2022-KR",
					"ISO-8859-1", "ISO-8859-13", "ISO-8859-15", "ISO-8859-2", "ISO-8859-3", "ISO-8859-4", "ISO-8859-5",
					"ISO-8859-6", "ISO-8859-7", "ISO-8859-8", "ISO-8859-9", "JIS_X0201", "JIS_X0212-1990", "KOI8-R", "KOI8-U",
					"Shift_JIS", "TIS-620", "US-ASCII", "UTF-16", "UTF-16BE", "UTF-16LE", "UTF-32", "UTF-32BE", "UTF-32LE",
					"UTF-8", "windows-1250", "windows-1251", "windows-1252", "windows-1253", "windows-1254", "windows-1255",
					"windows-1256", "windows-1257", "windows-1258", "windows-31j", "x-Big5-HKSCS-2001", "x-Big5-Solaris",
					"x-COMPOUND_TEXT", "x-euc-jp-linux", "x-EUC-TW", "x-eucJP-Open", "x-IBM1006", "x-IBM1025", "x-IBM1046",
					"x-IBM1097", "x-IBM1098", "x-IBM1112", "x-IBM1122", "x-IBM1123", "x-IBM1124", "x-IBM1166", "x-IBM1364",
					"x-IBM1381", "x-IBM1383", "x-IBM300", "x-IBM33722", "x-IBM737", "x-IBM833", "x-IBM834", "x-IBM856",
					"x-IBM874", "x-IBM875", "x-IBM921", "x-IBM922", "x-IBM930", "x-IBM933", "x-IBM935", "x-IBM937", "x-IBM939",
					"x-IBM942", "x-IBM942C", "x-IBM943", "x-IBM943C", "x-IBM948", "x-IBM949", "x-IBM949C", "x-IBM950",
					"x-IBM964", "x-IBM970", "x-ISCII91", "x-ISO-2022-CN-CNS", "x-ISO-2022-CN-GB", "x-iso-8859-11", "x-JIS0208",
					"x-JISAutoDetect", "x-Johab", "x-MacArabic", "x-MacCentralEurope", "x-MacCroatian", "x-MacCyrillic",
					"x-MacDingbat", "x-MacGreek", "x-MacHebrew", "x-MacIceland", "x-MacRoman", "x-MacRomania", "x-MacSymbol",
					"x-MacThai", "x-MacTurkish", "x-MacUkraine", "x-MS932_0213", "x-MS950-HKSCS", "x-MS950-HKSCS-XP",
					"x-mswin-936", "x-PCK", "x-SJIS_0213", "x-UTF-16LE-BOM", "X-UTF-32BE-BOM", "X-UTF-32LE-BOM",
					"x-windows-50220", "x-windows-50221", "x-windows-874", "x-windows-949", "x-windows-950",
					"x-windows-iso2022jp"
				],
				priority: [{
					key: 'low',
					label: '低',
				}, {
					key: 'high',
					label: '高',
				}, {
					key: 'normal',
					label: '一般',
				}],
				importance: [{
					key: 'low',
					label: '低',
				}, {
					key: 'high',
					label: '高',
				}, {
					key: 'normal',
					label: '一般',
				}],
				sensitivity: [{
					key: 'normal',
					label: '正常',
				}, {
					key: 'personal',
					label: '个人的',
				}, {
					key: 'private',
					label: '私人',
				}, {
					key: 'company-confidential',
					label: '机密',
				}],
				dynamicZipFilename: [],
				dynamicWildcard: [],
				dynamicFieldname: [],
				attachContentFileNameField: [],
				attachContentField: [],
				restaurants: [],
				queryParams: {
					fileTypetu: "",
					fileType: "",
				},
				fileType: "*",
				fileTypetu: 'png',

				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						trigger: 'blur'
					}],
				},
				filedatas: {},
				fileFlag: ""
			}
		},
		mounted() {
			this.init();
			this.getFileByFileType();
		},
		methods: {


			//点击事件
			change_use_auth() {
				if (this.step.use_auth == true) {
					this.authu = false;
					this.yshi = false;
					this.password = false;
				} else if (this.step.use_auth == false) {
					this.authu = true;
					this.yshi = true;
					this.password = true;
				}
			},
			sy() {
				if (this.step.use_secure_auth == true) {
					this.secure = false;
				} else if (this.step.use_secure_auth == false) {
					this.secure = true;
				}
			},
			youxian() {
				if (this.step.use_Priority == true) {
					this.im = false;
					this.pr = false;
					this.se = false;
				} else if (this.step.use_Priority == false) {
					this.im = true;
					this.pr = true;
					this.se = true;
				}
			},
			geshi() {
				if (this.step.use_HTML == false) {
					this.inputwen = true;
					this.wen = true;
					this.ro = true;
					this.tu = true;
					this.ne = true;
					this.html = true;
				} else if (this.step.use_HTML == true) {
					this.inputwen = false;
					this.wen = false;
					this.ro = false;
					this.tu = false;
					this.ne = false;
					this.html = false;
				}
			},
			fujia() {
				if (this.step.attachContentFromField == true) {
					this.nz = false;
					this.wz = false;
					this.dt = true;
					this.mz = true;
					this.tp = true;
					this.fuwen = false;
					this.zimu = true;
					this.tpfu = false;
					this.zwen = true;
					this.zwenm = true;
					this.zwz = true;
					this.zwjm = true;
					this.fjdy = true;
				} else if (this.step.attachContentFromField == false) {
					this.nz = true;
					this.wz = true;
					this.dt = false;
					this.mz = true;
					this.tp = true;
					this.fuwen = false;
					this.zimu = false;
					this.tpfu = false;
					this.zwen = false;
					this.zwenm = true;
					this.zwz = true;
					this.zwjm = true;
					this.fjdy = true;
				}
			},
			dongtai() {
				if (this.step.isFilenameDynamic == true) {
					this.mz = false;
					this.tp = false;
					this.fuwen = true;
					this.zimu = false;
					this.tpfu = true;
				} else if (this.step.isFilenameDynamic == false) {
					this.mz = true;
					this.tp = true;
					this.fuwen = false;
					this.zimu = false;
					this.tpfu = false;
				}
			},
			zipWen() {
				if (this.step.zip_files == true) {
					this.zwenm = false;
					this.zwjm = false;
					this.fjdy = false;
				} else if (this.step.zip_files == false) {
					this.zwenm = true;
					this.zwjm = true;
					this.fjdy = true;
				}
			},
			zLast() {
				if (this.step.zipFilenameDynamic == true) {
					this.zwz = false;
				} else if (this.step.zipFilenameDynamic == false) {
					this.zwz = true;
				}
			},



			//移除文件
			deleteFile(index, data) {
				data.forEach((item, i) => {
					if (i === index) {
						data.splice(index, 1);
					}
				})
			},
			//文件名目录上传文件
			changeFilesource(type) {
				if (type === 'file') {
					this.fileFolder = "0";
					this.selectFileVisiablefile = true;
					this.queryParams = {
						fileType: "*",
						isFolder: "0",
					};
					this.fileFlag = "file";
				} else {
					this.fileFlag = "Folder";
					this.fileFolder = "1";
					this.selectFileVisiablefolder = true;
				}
			},


			blurInputfile() {
				let flag = true;
				this.restaurantsfile.forEach((item, index) => {
					if (item.fileName === this.step.sourcefilefoldername) {
						flag = false;
					}
				});
				if (flag) {
					// this.step.showFileName = "";
				}
			},
			querySearchfile(queryString, cb) {
				var restaurants = this.restaurantsfile;
				var results = queryString ?
					restaurants.filter(this.createFilter(queryString)) :
					restaurants;
				// 调用 callback 返回建议列表的数据
				cb(results);
			},
			createFilter(queryString) {
				return (restaurant) => {
					return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
				};
			},
			handleSelectfile(item) {
				this.FileSelect.push({
					filePath: item.fileRelativPath + '/' + item.fileName,
					fileId: item.fileId,
					file: item.fileName,
				});
				// this.showFields = [];
				this.step.sourcefilefoldername = item.fileName;
				this.fileId = item.fileId;
				// this.step.file.name = item.fileRelativPath;
				this.step.fileId = item.fileId;
				this.step.filename = item.fileName;
				this.step.filePath = item.fileRelativPath;
				this.step.file_server_type = item.fileServerType;
				this.step.ftp_username = item.userName;
				this.step.ftp_password = item.password;
				// this.step.sheets.sheet['name'] = "";
				// this.step.showFileName = '';
			},

			getFileByFileType(fileId) {
				let queryParams = {
					fileType: "*",
					isFolder: "0",
				};
				let fileList = [];
				selectFileListByType(queryParams).then((res) => {
					if (res.data.code === "10000") {
						if (fileId !== undefined) {
							res.data.content.forEach((item, index) => {
								fileList.push({
									value: item.fileName,
									fileName: item.fileName,
									fileId: item.fileId,
									fileRelativPath: item.fileRelativPath,
									fileServerType: item.fileServerType,
									userName: item.userName,
									password: item.password,
								});
							});
							res.data.content.forEach((item, index) => {
								if (item.fileId === fileId) {
									this.handleSelectfile(item);
								}
							});
						} else {
							res.data.content.forEach((item, index) => {
								fileList.push({
									value: item.fileName,
									fileName: item.fileName,
									fileId: item.fileId,
									fileServerType: item.fileServerType,
									fileRelativPath: item.fileRelativPath,
									userName: item.userName,
									password: item.password,
								});
							});
						}
						this.restaurantsfile = fileList;
					} else {
						this.$message({
							message: "获取文件列表失败",
							type: "error",
						});
					}
				});
			},
			//获取文件名传入input框
			getFileData(fileData) {
				this.filedatas = fileData;
				if (this.fileFlag == "pic") {
					this.selectFileVisiabletufile = false;

					this.step.showFileName = fileData.fileName;
				} else if (this.fileFlag == "file") {
					this.selectFileVisiablefile = false;

					this.step.sourcefilefoldername = fileData.fileName;
				} else {
					this.selectFileVisiablefolder = false;
					this.step.sourcefilefoldername = fileData.fileName;

				}
				let fileId = fileData.fileId;
				this.getFileByFileType(fileId);

			},
			//图片文件
			changeFiletu() {
				this.fileFlag = "pic";
				this.queryParams = {
					fileTypetu: "png",
					isFolder: "0",
				};
				this.selectFileVisiabletufile = true;
			},

			blurInput() {
				let flag = true;
				this.restaurants.forEach((item, index) => {
					if (item.fileName === this.step.showFileName) {
						flag = false;
					}
				});
				if (flag) {
					// this.step.showFileName = "";
				}
			},
			querySearch(queryString, cb) {
				var restaurants = this.restaurants;
				var results = queryString ?
					restaurants.filter(this.createFiltertu(queryString)) :
					restaurants;
				// 调用 callback 返回建议列表的数据
				cb(results);
			},
			createFiltertu(queryString) {
				return (restaurant) => {
					return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
				};
			},
			handleSelect(item) {
				this.FileSelect.push({
					filePath: item.fileRelativPath + '/' + item.fileName,
					fileId: item.fileId,
					file: item.fileName,
					GeneralSymbols: '',
					GSymbolsEx: '',
					Require: 'N',
					Obtain: 'N'
				});
				// this.showFields = [];
				this.step.showFileName = item.fileName;
				this.fileId = item.fileId;
				// this.step.file.name = item.fileRelativPath;
				this.step.embeddedimages.embeddedimage.push({
					image_name: item.fileName,
					name: item.fileRelativPath,
					file_required: 'N',
					include_subfolders: 'N',
				});
				this.step.fileId = item.fileId;
				this.step.filename = item.fileName;
				this.step.filePath = item.fileRelativPath;
				this.step.file_server_type = item.fileServerType;
				this.step.ftp_username = item.userName;
				this.step.ftp_password = item.password;
				// this.step.sheets.sheet['name'] = "";
				// this.step.showFileName = '';
			},
			// getFileByFileType(fileId) {
			// 	let queryParams = {
			// 		fileType: "png",
			// 		isFolder: "0",
			// 	};
			// 	let fileList = [];
			// 	selectFileListByType(queryParams).then((res) => {
			// 		if (res.data.code == "10000") {
			// 			if (fileId !== undefined) {
			// 				res.data.content.forEach((item, index) => {
			// 					fileList.push({
			// 						value: item.fileName,
			// 						fileName: item.fileName,
			// 						fileId: item.fileId,
			// 						fileRelativPath: item.fileRelativPath,
			// 						fileServerType: item.fileServerType,
			// 						userName: item.userName,
			// 						password: item.password,
			// 					});
			// 				});
			// 				res.data.content.forEach((item, index) => {
			// 					if (item.fileId === fileId) {
			// 						this.handleSelect(item);
			// 					}
			// 				});
			// 			} else {
			// 				res.data.content.forEach((item, index) => {
			// 					fileList.push({
			// 						value: item.fileName,
			// 						fileName: item.fileName,
			// 						fileId: item.fileId,
			// 						fileServerType: item.fileServerType,
			// 						fileRelativPath: item.fileRelativPath,
			// 						userName: item.userName,
			// 						password: item.password,
			// 					});
			// 				});
			// 			}
			// 			this.restaurants = fileList;
			// 		} else {
			// 			console.log(111111);
			// 			this.$message({
			// 				message: "获取文件列表失败",
			// 				type: "error",
			// 			});
			// 		}
			// 	});
			// },

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

				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				if (obj.initValid == undefined) {
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.destination.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.destinationBCc.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.destinationCc.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.dynamicZipFilename.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.dynamicWildcard.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.dynamicFieldname.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.attachContentFileNameField.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.attachContentField.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.restaurants.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.FileSelect.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.replytoname.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.replyto.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.replyToAddresses.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.contact_person.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.contact_phone.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.server.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.port.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.auth_user.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.auth_password.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.subject.push({
								name: item.name,
							})
						});
						this.lastStepFields.forEach(item => {
							this.comment.push({
								name: item.name,
							})
						});
					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.destination.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.destinationBCc.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.destinationCc.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.dynamicZipFilename.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.dynamicWildcard.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.dynamicFieldname.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.attachContentFileNameField.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.attachContentField.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.restaurants.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.FileSelect.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.replytoname.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.replyto.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.replyToAddresses.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.contact_person.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.contact_phone.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.server.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.port.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.auth_user.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.auth_password.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.subject.push({
							name: item.name,
						})
					});
					this.lastStepFields.forEach(item => {
						this.comment.push({
							name: item.name,
						})
					});
					this.step.embeddedimages.embeddedimage = JSON.parse(JSON.stringify(obj.embeddedimages.embeddedimage));
					// this.step = obj;
				}
				if (curstep !== undefined && curstep.sensitivity!==undefined) {
					this.step.name = curstep.name;
					this.step.initFlag = curstep.initFlag;
					this.step.description = curstep.description;
					this.step.distribute = curstep.distribute;
					this.step.copies = curstep.copies;
					this.step.partitioning = curstep.partitioning;
					this.step.model_name = curstep.model_name;
					this.step.remotesteps = curstep.remotesteps;
					this.step.GUI = curstep.GUI;
					this.step.showFileName = curstep.showFileName;
					this.step.destination = curstep.destination;
					this.step.destinationBCc = curstep.destinationBCc;
					this.step.destinationCc = curstep.destinationCc;
					this.step.replytoname = curstep.replytoname;
					this.step.replyto = curstep.replyto;
					this.step.replyToAddresses = curstep.replyToAddresses;
					this.step.contact_phone = curstep.contact_phone;
					this.step.contact_person = curstep.contact_person;
					this.step.server = curstep.server;
					this.step.port = curstep.port;
					this.step.isFilenameDynamic = curstep.isFilenameDynamic;
					this.step.sourcewildcard = curstep.sourcewildcard;
					this.step.use_auth = curstep.use_auth;
					this.step.auth_user = curstep.auth_user;
					this.step.auth_password = curstep.auth_password;
					this.step.use_secure_auth = curstep.use_secure_auth;
					this.step.secureconnectiontype = curstep.secureconnectiontype;
					this.step.include_date = curstep.include_date;
					this.step.only_comment = curstep.only_comment;
					this.step.use_HTML = curstep.use_HTML;
					this.step.attachContentFromField = curstep.attachContentFromField;
					this.step.encoding = curstep.encoding;
					this.step.use_Priority = curstep.use_Priority;
					this.step.priority = curstep.priority;
					this.step.importance = curstep.importance;
					this.step.sensitivity = curstep.sensitivity;
					this.step.subject = curstep.subject;
					this.step.comment = curstep.comment;
					this.step.attachContentField = curstep.attachContentField;
					this.step.attachContentFileNameField = curstep.attachContentFileNameField;
					this.step.dynamicFieldname = curstep.dynamicFieldname;
					this.step.dynamicWildcard = curstep.dynamicWildcard;
					this.step.dynamicZipFilename = curstep.dynamicZipFilename;
				}
				for (let key in this.step) {
					if (this.step[key] == 'Y') {
						this.step[key] = true;
					} else if (this.step[key] == 'N') {
						this.step[key] = false;
					}
				}
				this.change_use_auth();
				this.sy();
				this.geshi();
				this.youxian();
				this.fujia();
				this.dongtai();
				this.zipWen();
				this.zLast();
			},
			submit() {
				this.step.initFlag = true;
				this.step.initValid = false;
				let step = Object.assign({}, this.step);
				if (this.step.use_auth == true) {
					step.use_auth = 'Y'
				} else {
					step.use_auth = 'N'
				}
				if (this.step.include_date == true) {
					step.include_date = 'Y'
				} else {
					step.include_date = 'N'
				}
				if (this.step.only_comment == true) {
					step.only_comment = 'Y'
				} else {
					step.only_comment = 'N'
				}
				if (this.step.use_HTML == true) {
					step.use_HTML = 'Y'
				} else {
					step.use_HTML = 'N'
				}
				if (this.step.attachContentFromField == true) {
					step.attachContentFromField = 'Y'
				} else {
					step.attachContentFromField = 'N';
				}
				if (this.step.use_Priority == true) {
					step.use_Priority = 'Y'
				} else {
					step.use_Priority = 'N'
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
	input[type="file"] {
		display: none;
	}
</style>
