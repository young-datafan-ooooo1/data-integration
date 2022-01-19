<template>
	<div class="plugin_body">
		<div class="plugin_content div_form">
			<el-form ref="step" :model="step" size="mini" label-width="30%" :rules="rules">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;"></el-input>
				</el-form-item>
			</el-form>
			<el-tabs @tab-click="handleClick">
				<el-tab-pane label="选择和修改">
					<div>
						<div>
							<div style="display: inline-block;">字段：</div>
							<div style="display: inline-block; float: right; margin-bottom: 12px;">
								<el-button size="mini" type="primary" @click="get">获取选择的字段</el-button>
								<el-button size="mini" @click="mapping= true">列映射</el-button>
								<el-dialog title="列映射" :visible.sync="mapping" width="30%" :modal-append-to-body="false">
									<div>
										<div align="center">
											<i class="el-icon-warning"></i>列映射只支持一个输出节点
										</div>
										<div slot="footer" class="dialog-footer" align="right">
											<el-button @click="mapping = false" size="mini">关 闭</el-button>
										</div>
									</div>
								</el-dialog>
							</div>
						</div>
						<el-table :data="step.fields.field" border height="330" :header-cell-style="{background:'#eef1f6'}">
							<el-table-column type="index" label="#" width="50"></el-table-column>
							<el-table-column prop="name" label="字段名称">
								<template slot-scope="scope">
									<el-select v-model="scope.row.name" size="mini" filterable allow-create clearable>
										<el-option v-for="item in fieldName" :key="item.name" :label="item.name" :value="item.name"></el-option>
									</el-select>
								</template>
							</el-table-column>
							<el-table-column prop="rename" label="改名成">
								<template slot-scope="scope">
									<el-input v-model="scope.row.rename" clearable size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="length" label="长度">
								<template slot-scope="scope">
									<el-input v-model="scope.row.length" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column prop="accuracy" label="精度">
								<template slot-scope="scope">
									<el-input v-model="scope.row.accuracy" size="mini"></el-input>
								</template>
							</el-table-column>
							<el-table-column label="操作" width="100px">
								<template slot-scope="scope">
									<el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
					</div>
					<el-checkbox label="包含未指定的列，按名称排序" v-model="step.select_unspecified"></el-checkbox>
				</el-tab-pane>
				<el-tab-pane label="移除">
					<div>
						<div>
							<div style="display: inline-block;">移除的字段：</div>
							<div style="display: inline-block; float: right; margin-bottom: 12px;">
								<el-button size="mini" type="primary" @click="getremove">获取移除的字段</el-button>
							</div>
						</div>
						<div>
							<el-table :data="step.fields.remove" border height="330" :header-cell-style="{background:'#eef1f6'}">
								<el-table-column type="index" label="#" width="50"></el-table-column>
								<el-table-column prop="name" label="字段名称">
									<template slot-scope="scope">
										<el-select v-model="scope.row.name" size="mini" filterable allow-create clearable>
											<el-option v-for="item in removeName" :key="item.name" :label="item.name" :value="item.name"></el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column label="操作" width="100px">
									<template slot-scope="scope">
										<el-button size="mini" type="text" @click="deleteRowRemove(scope.$index)">删除</el-button>
									</template>
								</el-table-column>
							</el-table>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="元数据">
					<div>
						<div>
							<div style="display: inline-block;">需要改变元数据的字段：</div>
							<div style="display: inline-block;float: right; margin-bottom: 12px;">
								<el-button size="mini" type="primary" @click="getmeta">获取改变的字段</el-button>
							</div>
						</div>
						<div>
							<el-table :data="step.fields.meta" border height="330" :header-cell-style="{background:'#eef1f6'}">
								<el-table-column type="index" label="#" width="50"></el-table-column>
								<el-table-column prop="name" label="字段名称" width="100">
									<template slot-scope="scope">
										<el-select v-model="scope.row.name" size="mini" filterable allow-create clearable>
											<el-option v-for="item in metaName" :key="item.name" :label="item.name" :value="item.name"></el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="rename" label="改名成">
									<template slot-scope="scope">
										<el-input v-model="scope.row.rename" size="mini" clearable></el-input>
									</template>
								</el-table-column>
								<el-table-column prop="type" label="类型">
									<template slot-scope="scope">
										<el-select v-model="scope.row.type" size="mini" style="width: 100%;" clearable>
											<el-option v-for="item in type" :key="item" :label="item" :value="item">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="length" label="长度">
									<template slot-scope="scope">
										<el-input v-model="scope.row.length" size="mini"></el-input>
									</template>
								</el-table-column>
								<el-table-column prop="accuracy" label="精度">
									<template slot-scope="scope">
										<el-input v-model="scope.row.accuray" size="mini"></el-input>
									</template>
								</el-table-column>
								<el-table-column prop="normal" label="二进制到普通?" width="180">
									<template slot-scope="scope">
										<el-select v-model="scope.row.normal" size="mini" style="width: 100%;" clearable>
											<el-option v-for="item in normal" :key="item.key" :label="item.label" :value="item.key">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="format" label="格式">
									<template slot-scope="scope">
										<el-select v-model="scope.row.formet" size="mini" style="width: 100%;" clearable>
											<el-option v-for="itme in format" :key="item" :label="item" :value="item">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="date_format_lenient" label="日期格式宽松?" width="180">
									<template slot-scope="scope">
										<el-select v-model="scope.row.date_format_lenient" size="mini" style="width: 100%;" clearable>
											<el-option v-for="item in date_format_lenient" :key="item.key" :label="item.label" :value="item.key">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="date_format_locale" label="日期区域设置" width="150">
									<template slot-scope="scope">
										<el-select v-model="scope.row.date_format_locale" size="mini" style="width: 100%;" clearable>
											<el-option v-for="item in date_format_locale" :key="item" :label="item" :value="item">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="date_format_timezone" label="日期时区" width="160">
									<template slot-scope="scope">
										<el-select v-model="scope.row.date_format_timezone" size="mini" style="width: 100%;" clearable>
											<el-option v-for="item in date_format_timezone" :key="item" :label="item" :value="item">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="lenient_string_to_number" label="宽松的数字转换?" width="180">
									<template slot-scope="scope">
										<el-select v-model="scope.row.lenient_string_to_number" size="mini" style="width: 100%;" clearable>
											<el-option v-for="item in lenient_string_to_number" :key="item.key" :label="item.label" :value="item.key">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="encoding" label="编码" width="120">
									<template slot-scope="scope">
										<el-select v-model="scope.row.encoding" size="mini" style="width: 100%;" clearable>
											<el-option v-for="item in encoding" :key="item" :label="item" :value="item">
											</el-option>
										</el-select>
									</template>
								</el-table-column>
								<el-table-column prop="decimal_symbol" label="十进制">
									<template slot-scope="scope">
										<el-input v-model="scope.row.decimal_symbol" size="mini"></el-input>
									</template>
								</el-table-column>
								<el-table-column prop="grouping_symbol" label="分组">
									<template slot-scope="scope">
										<el-input v-model="scope.row.grouping_symbol" size="mini"></el-input>
									</template>
								</el-table-column>
								<el-table-column prop="currency_symbol" label="货币">
									<template slot-scope="scope">
										<el-input v-model="scope.row.currency_symbol" size="mini"></el-input>
									</template>
								</el-table-column>
								<el-table-column label="操作" width="100px">
									<template slot-scope="scope">
										<el-button size="mini" type="text" @click="deleteRowMeta(scope.$index)">删除</el-button>
									</template>
								</el-table-column>
							</el-table>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>
		<div slot="footer" class="dialog-footer plugin_footer">
			<el-button @click="closeDialog" size="small">取 消</el-button>
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
				key: '', //插件对应的画布
				nodeData: {},
				mapping: false,
				fieldName: [],
				metaName: [],
				removeName: [],
				lenient_string_to_number: [{
					key: 'true',
					label: '是'
				}, {
					key: 'false',
					label: '否'
				}],
				date_format_lenient: [{
					key: 'true',
					label: '是'
				}, {
					key: 'false',
					label: '否'
				}],
				normal: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				type: ["BigNumber", "Binary", "Boolean", "Date", "Integer", "Internet Address", "Number", "String", "Serializable",
					"Timestamp"
				],
				format: [],
				date_format_locale: ["ar", "ar_AE", "ar_BH", "ar_DZ", "ar_IQ", "ar_JO", "ar_EG", "ar_KW", "ar_LB", "ar_MA", "ar_LY",
					"ar_OM",
					"ar_QA",
					"ar_SA", "ar_SD", "ar_SY", "ar_TN", "ar_YE", "be", "be_BY", "bg", "bg_BG", "ca", "ca_ES", "cs", "cs_Cz", "da",
					"da_DK", "de", "de_AT", "ide_CH", "ide_DE", "de_GR", "de_Lu", "el", "el_CY",
					"el_GR", "en", "en_AU", "en_CA", "en_GB", "en_IE", "en_IN", "en_MT", "en_Nz", "en_PH", "en_sG", "en_us", "en_ZA",
					"es", "es_AR", "es_Bo", "es_CL", "es_co", "es_CR",
					"es_cu", "es_Do", "es_EC", "es_ES",
					"es_GT", "es_HN", "es_MX", "es_Nl", "es_PA", "es_PE", "es_PR", "es_PY", "es_sv", "es_us", "es_uy", "es_VE", "et",
					"et_EE", "fi", "fi_Fl",
					"fr", "fr_BE", "fr_CA", "fr_CH", "fr_FR", "fr_Lu", "ga", "ga_lE", "hi", "hi_IN", "hr", "hr_HR", "hu", "hu_Hu",
					"in", "in_ID", "is", "is_Is", "it", "it_CH", "it_IT",
					"iw", "iw_IL", "ja", "ja_Jp", "ja_JP_JP_#u-ca-japanese", "ko", "ko_KR", "lt", "lt_LT", "lv", "lv_Lv", "mk",
					"mk_MK", "ms", "ms_MY", "mt", "mt_MT", "nl", "nl_BE", "nl_NL", "no", "no_No", "no_NO_NY", "pl", "pl_PL", "pt",
					"pt_BR",
					"pt_PT", "ro", "ro_RO", "ru", "ru_RU", "sk", "sk_SK", "sl", "sl_SI", "sq", "sq_AL", "sr", "sr_BA", "sr_BA_#Latn",
					"sr_Cs", "sr_ME", "sr_ME_#Latn",
					"sr_RS", "sr_RS_#Latn", "sr_#Latn", "sv", "sv_SE", "th", "th_TH", "th_TH_TH_#u-nu-thai", "tr", "tr_TR", "uk",
					"uk_UA",
					"vi", "vi_VN", "zh", "zh_CN", "zh_HK", "zh_SG", "zh_Tw"
				],
				date_format_timezone: ["ACT", "AET", "AGT", "ART", "AST", "Africa/Abidjan", "Africa/Accra", "Africa/Addis_Ababa",
					"Africa/Algiers",
					"Africa/Asmara", "Africa/Asmera", "Africa/Bamako", "Africa/Bangui", "Africa/Banjul", "Africa/Bissau",
					"Africa/Blantyre", "Africa/Brazzaville", "Africa/Bujumbura", "Africa/Cairo", "Africa/Casablanca", "Africa/Ceuta",
					"Africa/ConaKry", "Africa/Dakar", "Africa/Dar_es_Salaam",
					"Africa/Djibouti", "Africa/Douala", "Africa/El_Aaiun", "Africa/Freetown", "Africa/Gaborone", "Africa/Harare",
					"Africa/Johannesburg", "Africa/Juba", "Africa/Kampala",
					"Africa/Khartoum", "Africa/Kigali", "Africa/Kinshasa", "Africa/Lagos", "Africa/Libreville", "Africa/Lome",
					"Africa/Luanda", "Africa/Lubumbashi", "Africa/Lusaka", "Africa/Malabo",
					"Africa/Maputo", "Africa/Maseru", "Africa/Mbabane", "frica/Mogadishu", "Africa/Monrovia", "Africa/Nairobi",
					"Africa/Ndjamena", "frica/Niamey", "Africa/Nouakchott", "Africa/Ouagadougou",
					"Africa/Porto-Novo", "Africa/Sao_Tome", "Africa/Timbuktu", "frica/Tripoli", "Africa/Tunis", "Africa/Windhoek",
					"America/Adak", "America/Anchorage", "America/Anguilla", "America/Antigua",
					"America/Araguaina", "America/Argentina/Buenos_Aires", "America/Argentina/Catamarca",
					"America/Argentina/ComodRivadavia", "America/Argentina/Cordoba", "America/Argentina/Jujuy",
					"America/Argentina/La_Rioja",
					"America/Argentina/Mendoza", "America/Argentina/Rio_Gallegos", "America/Argentina/Salta",
					"America/Argentina/San_Juan", "America/Argentina/San_Luis", "America/Argentina/Tucuman",
					"America/Argentina/Ushuaia",
					"America/Aruba", "America/Asuncion", "America/Atikokan", "America/Atka", "America/Bahia",
					"America/Bahia_Banderas", "America/Barbados", "America/Belem", "America/Belize", "America/Blanc-Sablon",
					"America/Boa_Vista", "America/Bogota",
					"America/Boise", "America/Buenos_Aires", "America/Cambridge_Bay", "America/Campo_Grande", "America/Cancun",
					"America/Caracas", "America/Catamarca", "America/Cayenne", "America/Cayman", "America/Chicago",
					"America/Chihuahua", "America/Coral_Harbour", "America/Cordoba", "America/Costa_Rica", "America/Creston",
					"America/Cuiaba", "America/Curacao", "America/Danmarkshavn", "America/Dawson", "America/Dawson_Creek",
					"America/Denver",
					"America/Detroit", "America/Dominica", "America/Edmonton", "America/Eirunepe", "America/El_Salvador",
					"America/Ensenada", "America/Fort_Nelson", "America/Fort_Wayne", "America/Fortaleza", "America/Glace_Bay",
					"America/Godthab",
					"America/Goose_Bay", "America/Grand_Turk", "America/Grenada", "America/Guadeloupe", "America/Guatemala",
					"America/Guayaquil", "America/Guyana", "America/Halifax", "America/Havana", "America/Hermosillo",
					"America/Indiana/Indianapolis", "America/Indiana/Knox", "America/Indiana/Marengo", "America/Indiana/Tell_City",
					"America/lIndiana/Vevay", "America/Indiana/Vincennes", "America/Indiana/Winamac", "America/Indianapolis",
					"America/Inuvik",
					"America/lqaluit", "America/Jamaica", "America/Jujuy", "America/Juneau", "America/Kentucky/Louisville",
					"America/Kentucky/Monticello", "America/Knox_INAmerica/KralendijkAmerica/La_PazAmerica/Lima",
					"America/Los_Angeles", "America/Louisville",
					"America/Lower_Princes", "America/Maceio", "America/Managua", "America/Manaus", "America/Martinique",
					"America/Matamoros", "America/Mazatlan", "America/Mendoza", "America/Menominee", "America/Merida",
					"America/Metlakatla", "America/Mexico_City",
					"America/Miquelon", "America/Moncton", "America/Monterrey", "America/Montevideo", "America/Montreal",
					"America/Montserrat", "America/Nassau", "America/New_York", "America/Nipigon", "America/Nome", "America/Noronha",
					"America/North_Dakota/Beulah",
					"America/North_Dakota/Center", "America/North_Dakota/New_Salem", "America/Panama", "America/Pangnirtung",
					"America/Paramaribo", "America/Phoenix", "America/Port-au-Prince", "America/Port_of_Spain", "America/Porto_Acre",
					"America/Porto_Velho",
					"America/Puerto_Rico", "America/Punta_Arenas", "America/Rainy_River", "America/Rankin_Inlet", "America/Recife",
					"America/Regina", "America/Resolute", "America/Rio_Branco", "America/Rosario", "America/Santa_lsabel",
					"America/Santarem", "America/Santiago",
					"America/Santo_Domingo", "America/Sao_Paulo", "America/Scoresbysund", "America/Shiprock", "America/Sitka",
					"America/St_Barthelemy", "America/St_Johns", "America/st_Kitts", "America/St_Lucia", "America/St_Thomas",
					"America/st_Vincent", "America/Swift_Current",
					"America/Tegucigalpa", "America/Thule", "America/Thunder_Bay", "America/Tijuana", "America/Toronto",
					"America/Tortola", "America/Vancouver", "America/Virgin", "America/Whitehorse", "America/Winnipeg",
					"America/Yakutat", "America/Yellowknife",
					"Antarctica/Casey", "Antarctica/Davis", "Antarctica/DumontDUrville", "Antarctica/Macquarie", "Antarctica/Mawson",
					"Antarctica/McMurdo", "Antarctica/Palmer", "Antarctica/Rothera", "Antarctica/South_Pole", "Antarctica/Syowa",
					"Antarctica/Troll", "Antarctica/Vostok", "Arctic/Longyearbyen",
					"Asia/Aden", "Asia/Almaty", "Asia/Amman", "Asia/Anadyr", "Asia/Aqtau", "Asia/Aqtobe", "Asia/Ashgabat",
					"Asia/Ashkhabad", "Asia/Atyrau", "Asia/Baghdad", "Asia/Bahrain", "Asia/Baku", "Asia/Bangkok", "Asia/Barnaul",
					"Asia/Beirut", "Asia/Bishkek", "Asia/Brunei", "Asia/Calcutta", "Asia/Chita",
					"Asia/Choibalsan", "Asia/Chongqing", "Asia/Chungking", "Asia/Colombo", "Asia/Dacca", "Asia/Damascus",
					"Asia/Dhaka", "Asia/Dili", "Asia/Dubai", "Asia/Dushanbe", "Asia/Famagusta", "Asia/Gaza", "Asia/Harbin",
					"Asia/Hebron", "Asia/Ho_Chi_Minh", "Asia/Hong_Kong", "Asia/Hovd", "Asia/Irkutsk",
					"Asia/lstanbul", "Asia/Jakarta", "Asia/Jayapura", "Asia/Jerusalem", "Asia/Kabul", "Asia/Kamchatka",
					"Asia/Karachi", "Asia/Kashgar", "Asia/Kathmandu", "Asia/Katmandu", "Asia/Khandyga", "Asia/Kolkata",
					"Asia/Krasnoyarsk", "Asia/Kuala_Lumpur", "Asia/Kuching", "Asia/Kuwait", "Asia/Macao",
					"Asia/Macau", "Asia/Magadan", "Asia/Makassar", "Asia/Manila", "Asia/Muscat", "Asia/Nicosia", "Asia/Novokuznetsk",
					"Asia/Novosibirsk", "Asia/Omsk", "Asia/Oral", "Asia/Phnom_Penh", "Asia/Pontianak", "Asia/Pyongyang", "Asia/Qatar",
					"Asia/Qyzylorda", "Asia/Rangoon", "Asia/Riyadh", "Asia/Saigon",
					"Asia/Sakhalin", "Asia/Samarkand", "Asia/Seoul", "Asia/Singapore", "Asia/Srednekolymsk", "Asia/Taipei",
					"Asia/Tashkent", "Asia/Tbilisi", "Asia/Tehran", "Asia/Tel_Aviv", "Asia/Thimbu", "Asia/Thimphu", "Asia/Tokyo",
					"Asia/Tomsk", "Asia/Ujung_Pandang", "Asia/Ulaanbaatar", "Asia/Ulan_Bator",
					"Asia/Urumqi", "Asia/Ust-Nera", "Asia/Vientiane", "Asia/Vladivostok", "Asia/Yakutsk", "Asia/Yangon",
					"Asia/Yekaterinburg", "Asia/Yerevan", "Atlantic/Azores", "Atlantic/Bermuda", "Atlantic/Canary",
					"Atlantic/Cape_Verde", "Atlantic/Faeroe", "Atlantic/Faroe", "Atlantic/Jan_Mayen", "Atlantic/Madeira",
					"Atlantic/Reykjavik", "Atlantic/South_Georgia", "Atlantic/St_Helena", "Atlantic/Stanley", "Australia/ACT",
					"Australia/Adelaide", "Australia/Brisbane", "Australia/Broken_Hill", "Australia/Canberra", "Australia/Currie",
					"Australia/Darwin", "Australia/Eucla", "Australia/Hobart", "Australia/LHI",
					"Australia/Lindeman", "Australia/Lord_Howe", "Australia/Melbourne", "Australia/NsW", "Australia/North",
					"Australia/Perth", "Australia/Queensland", "Australia/South", "Australia/Sydney", "Australia/Tasmania",
					"Australia/Victoria", "Australia/West", "Australia/Yancowinna",
					"BET", "BST", "Brazil/Acre", "Brazil/DeNoronha", "Brazil/East", "Brazil/West", "CAT", "CET", "CNT", "CST",
					"CST6CDT", "CTT", "Canada/Atlantic", "Canada/Central", "Canada/East-Saskatchewan", "Canada/Eastern",
					"Canada/Mountain", "Canada/Newfoundland", "Canada/Pacific",
					"Canada/Saskatchewan", "Canada/Yukon", "Chile/Continental", "Chile/Easterlsland", "Cuba", "EAT", "ECT", "EET",
					"EST", "EST5EDT", "Egypt", "Eire", "Etc/GMT", "Etc/GMT+O", "Etc/GMT+1", "Etc/GMT+10", "Etc/GMT+11", "Etc/GMT+12",
					"Etc/GMT+2", "Etc/GMT+3", "Etc/GMT+4", "Etc/GMT+5", "Etc/GMT+6",
					"Etc/GMT+7", "Etc/GMT+8", "Etc/GMT+9", "Etc/GMT-O", "Etc/GMT-1", "Etc/GMT-10", "Etc/GMT-11", "Etc/GMT-12",
					"Etc/GMT-13", "Etc/GMT-14", "Etc/GMT-2", "Etc/GMT-3", "Etc/GMT-4", "Etc/GMT-5", "Etc/GMT-6", "Etc/GMT-7",
					"Etc/GMT-8", "Etc/GMT-9", "Etc/GMTO", "Etc/Greenwich", "Etc/UCT", "Etc/UTC",
					"Etc/Universal", "Etc/Zulu", "Europe/Amsterdam", "Europe/Andorra", "Europe/Astrakhan", "Europe/Athens",
					"Europe/Belfast", "Europe/Belgrade", "Europe/Berlin", "Europe/Bratislava", "Europe/Brussels",
					"Europe/Bucharest", "Europe/Budapest", "Europe/Busingen", "Europe/Chisinau", "Europe/Copenhagen", "Europe/Dublin",
					"Europe/Gibraltar", "Europe/Guernsey", "Europe/Helsinki", "Europe/lsle_of_Man",
					"Europe/Istanbul", "Europe/Jersey", "Europe/Kaliningrad", "Europe/Kiev", "Europe/Kirov", "Europe/Lisbon",
					"Europe/Ljubljana", "Europe/London", "Europe/Oslo", "Europe/Paris",
					"Europe/Luxembourg", "Europe/Madrid", "Europe/Malta", "Europe/Mariehamn", "Europe/Minsk", "Europe/Monaco",
					"Europe/Moscow", "Europe/Nicosia", "Europe/Prague ", "Europe/Riga ", "Europe/Rome ", "Europe/Samara ",
					"Europe/San_Marino ",
					"Europe/Sarajevo ", "Europe/Saratov ", "Europe/Simferopol ", "Europe/Skopje ", "Europe/Sofia ",
					"Europe/Stockholm ", "Europe/Tallinn ",
					"Europe/Tirane ", "Europe/Tiraspol ", "Europe/Ulyanovsk ", "Europe/Uzhgorod ", "Europe/Vaduz ", "Europe/Vatican ",
					"Europe/Vienna ",
					"Europe/Vilnius ", "Europe/Volgograd ", "Europe/Warsaw", "Europe/Zagreb", "Europe/Zaporozhye", "Europe/Zurich",
					"GB", "GB-Eire",
					"GMT", "GMTO", "Greenwich", "HST", "Hongkong", "IET", "IST", "lceland", "Indian/Antananarivo", "Indian/Chagos",
					"Indian/Christmas",
					"lndian/Cocos", "lndian/Comoro", "Indian/Kerguelen", "lndian/Mahe", "Indian/Maldives", "lndian/Mauritius",
					"lndian/Mayotte",
					"lndian/Reunion", "lran", "lsrael", "JST", "Jamaica", "Japan", "Kwajalein", "Libya", "MET", "MIT", "MST",
					"MST7MDT", "Mexico/BajaNorte",
					"Mexico/BajaSur", "Mexico/General", "NET", "NST", "NZ", "NZ-CHAT", "Navajo", "PLT", "PNT", "PRC", "PRT", "PST",
					"PST8PDT", "Pacific/Apia",
					"Pacific/Auckland", "Pacific/Bougainville", "Pacific/Chatham", "Pacific/Chuuk", "Pacific/Easter", "Pacific/Efate",
					"Pacific/Enderbury",
					"Pacific/Fakaofo", "Pacific/Fiji", "Pacific/Funafuti", "Pacific/Galapagos", "Pacific/Gambier",
					"Pacific/Guadalcanal", "Pacific/Guam",
					"Pacific/Honolulu", "Pacific/Johnston", "Pacific/Kiritimati", "Pacific/Kosrae", "Pacific/Kwajalein",
					"Pacific/Majuro", "Pacific/Marquesas",
					"Pacific/Midway", "Pacific/Nauru", "Pacific/Niue", "Pacific/Norfolk", "Pacific/Noumea", "Pacific/Pago_Pago",
					"Pacific/Palau", "Pacific/Pitcairn",
					"Pacific/Pohnpei", "Pacific/Ponape", "pacific/Port_Moresby", "Pacific/Rarotonga", "Pacific/Saipan",
					"Pacific/Samoa", "Pacific/Tahiti", "Pacific/Tarawa",
					"Pacific/Tongatapu", "Pacific/Truk", "Pacific/Wake", "Pacific/Wallis", "Pacific/Yap", "Poland", "Portugal", "ROK",
					"ssT", "singapore", "SystemV/AST4",
					"systemV/AST4ADT", "SystemV/CST6", "systemV/CST6CDT", "systemV/EST5", "SystemV/EST5EDT", "SystemV/HST10",
					"SystemV/MST7", "SystemV/MST7MDT",
					"SystemV/PST8", "SystemV/PST8PDT", "SystemV/YST9", "SystemV/YST9YDT", "Turkey", "uCT", "uS/Alaska", "uS/Aleutian",
					"US/Arizona", "uS/Central",
					"US/East-Indiana", "US/Eastern", "US/Hawaii", "uS/Indiana-Starke", "us/Michigan", "uS/Mountain", "uS/Pacific",
					"us/Pacific-New",
					"us/Samoa", "uTC", "Universal", "vST", "w-su", "WET", "Zulu"
				],
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

				step: {
					name: "字段选择",
					type: "SelectValues",
					description: [],
					distribute: "Y",
					custom_distribution: [],
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					fields: {
						field: [],
						remove: [],
						meta: [],
					},
					attributes: [],
					cluster_schema: [],
					remotesteps: {
						input: "\n      ",
						output: "\n      "
					},
					select_unspecified: false,
					GUI: {
						xloc: 288,
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
			handleClick(tab, event) {
				console.log(tab, event);
			},

			deleteRowRemove(index) { //移除一行
				this.step.fields.remove.splice(index, 1); //删掉该行
			},

			deleteRowMeta(index) { //移除一行
				this.step.fields.meta.splice(index, 1); //删掉该行
			},

			deleteRow(index) { //移除一行
				this.step.fields.field.splice(index, 1); //删掉该行
			},
			//刷新
			getreload() {
				this.step.fields.field = []
			},
			reload() {
				this.step.fields.remove = []
			},
			meload() {
				this.step.fields.meta = []
			},
			//选择
			get() {
				this.getreload();
				this.lastStepFields = JSON.parse(JSON.stringify(this.step['outFields']));
				this.lastStepFields.forEach(item => {
					this.step.fields.field.push({
						name: item.name,
						type: item.type,
						repeat: item.repeat,
						decimal: item.decimal,
						group: item.group,
						length: item.length,
					})
				});
				console.log(this.step.fields.field);
			},
			//移除
			getremove() {
				this.reload();
				if (this.step.fields.field !== undefined && this.step.fields.field.length > 0) {
					this.lastStepFields = this.step.fields.field;
					this.lastStepFields.forEach(item => {
						if (item.rename !== undefined) {
							this.step.fields.remove.push({
								name: item.rename,
								type: item.type,
								repeat: item.repeat,
								decimal: item.decimal,
								group: item.group,
								length: item.length,
							})
						} else {
							this.step.fields.remove.push({
								name: item.name,
								type: item.type,
								repeat: item.repeat,
								decimal: item.decimal,
								group: item.group,
								length: item.length,
							})
						}
					});
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(this.step['outFields']));
					this.lastStepFields.forEach(item => {
						this.step.fields.remove.push({
							name: item.name,
							type: item.type,
							repeat: item.repeat,
							decimal: item.decimal,
							group: item.group,
							length: item.length,
						})
					});
				}
			},
			//元数据
			getmeta() {
				this.meload();
				let fields = [];
				if (this.step.fields.field.length > 0) {
					this.step.fields.field.forEach(row => {
						let flag = true;
						this.step.fields.remove.forEach(removeRow => {
							if (row.rename !== undefined) {
								if (row.rename == removeRow.name) {
									flag = false;
								}
							} else {
								if (row.name == removeRow.name) {
									flag = false;
								}
							}
						});
						if (flag) {
							fields.push(row);
						}
					});
					fields.forEach(item => {
						if (item.rename !== undefined) {
							this.step.fields.meta.push({
								name: item.rename,
								type: item.type,
								repeat: item.repeat,
								decimal: item.decimal,
								group: item.group,
								length: item.length,
							})
						} else {
							this.step.fields.meta.push({
								name: item.name,
								type: item.type,
								repeat: item.repeat,
								decimal: item.decimal,
								group: item.group,
								length: item.length,
							})
						}
					});
				} else if (this.step.fields.remove.length > 0) {
					this.step['outFields'].forEach(row => {
						let flag = true;
						this.step.fields.remove.forEach(removeRow => {
							if (row.name == removeRow.name) {
								flag = false;
							}
						});
						if (flag) {
							fields.push(row);
						}
					});
					fields.forEach(item => {
						this.step.fields.meta.push({
							name: item.name,
							type: item.type,
							repeat: item.repeat,
							decimal: item.decimal,
							group: item.group,
							length: item.length,
						})
					});
				} else {
					fields = this.step['outFields'];
					fields.forEach(item => {
						this.step.fields.meta.push({
							name: item.name,
							type: item.type,
							repeat: item.repeat,
							decimal: item.decimal,
							group: item.group,
							length: item.length,
						})
					});
				}
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
					console.log(JSON.stringify(obj) + 11111112);
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					}
				} else {
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
					this.step.fields.meta = JSON.parse(JSON.stringify(obj.fields.meta));
					this.step.fields.remove = JSON.parse(JSON.stringify(obj.fields.remove));
					console.log(JSON.stringify(obj));
					// console.log(this.step.fields.field);
				}

				this.step.name = curstep.name;
			},

			submit() {

				this.step.outFields = [];
				let fields = [];
				if (this.step.fields.field.length > 0) {
					this.step.fields.field.forEach(row => {
						let flag = true;
						this.step.fields.remove.forEach(removeRow => {
							if (row.rename !== undefined) {
								if (row.rename == removeRow.name) {
									flag = false;
								}
							} else {
								if (row.name == removeRow.name) {
									flag = false;
								}
							}
						});
						if (flag) {
							fields.push(row);
						}
					});
					fields.forEach(item => {
						if (item.rename !== undefined) {
							this.step.outFields.push({
								name: item.rename,
								type: item.type,
								repeat: item.repeat,
								decimal: item.decimal,
								group: item.group,
								length: item.length,
							})
						} else {
							this.step.outFields.push({
								name: item.name,
								type: item.type,
								repeat: item.repeat,
								decimal: item.decimal,
								group: item.group,
								length: item.length,
							})
						}
					});
				} else if (this.step.fields.remove.length > 0) {
					this.step['outFields'].forEach(row => {
						let flag = true;
						this.step.fields.remove.forEach(removeRow => {
							if (row.name == removeRow.name) {
								flag = false;
							}
						});
						if (flag) {
							fields.push(row);
						}
					});
					fields.forEach(item => {
						this.step.outFields.push({
							name: item.name,
							type: item.type,
							repeat: item.repeat,
							decimal: item.decimal,
							group: item.group,
							length: item.length,
						})
					});
				} else {
					fields = this.step['outFields'];
					fields.forEach(item => {
						this.step.outFields.push({
							name: item.name,
							type: item.type,
							repeat: item.repeat,
							decimal: item.decimal,
							group: item.group,
							length: item.length,
						})
					});
				}

				console.log(this.step.outFields);
				this.step.initFlag = true;
				console.info("最终字段", this.step);
				this.step.initValid = false;
				let step = Object.assign({}, this.step);
				if (this.step.select_unspecified == true) {
					step.select_unspecified = 'Y'
				} else {
					step.select_unspecified = 'N'
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
	.el-checkbox {
		margin-top: 5px;
	}

	.el-form-item--mini.el-form-item {
		margin-bottom: 10px;
	}
</style>
