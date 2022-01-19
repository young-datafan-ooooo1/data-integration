var SIGN_REGEXP = /([yMdhsm])(\1*)/g;
var DEFAULT_PATTERN = 'yyyy-MM-dd';

function padding(s, len) {
	var len = len - (s + '').length;
	for(var i = 0; i < len; i++) {
		s = '0' + s;
	}
	return s;
};
export function generateTitle(title) {
	const hasKey = this.$te('/' + title)
	const translatedTitle = this.$t('/' + title) // $t :this method from vue-i18n, inject in @/lang/index.js
	if(hasKey) {
		return translatedTitle
	}
	return title
}

export default {
	getQueryStringByName: function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		var context = "";
		if(r != null)
			context = r[2];
		reg = null;
		r = null;
		return context == null || context == "" || context == "undefined" ? "" : context;
	},
	formatDate: {
		addDays: function(date, num) {
			date.setDate(date.getDate() + num);
		},
		format: function(date, pattern) {
			pattern = pattern || DEFAULT_PATTERN;
			return pattern.replace(SIGN_REGEXP, function($0) {
				switch($0.charAt(0)) {
					case 'y':
						return padding(date.getFullYear(), $0.length);
					case 'M':
						return padding(date.getMonth() + 1, $0.length);
					case 'd':
						return padding(date.getDate(), $0.length);
					case 'w':
						return date.getDay() + 1;
					case 'h':
						return padding(date.getHours(), $0.length);
					case 'm':
						return padding(date.getMinutes(), $0.length);
					case 's':
						return padding(date.getSeconds(), $0.length);
				}
			});
		},
		parse: function(dateString, pattern) {
			var matchs1 = pattern.match(SIGN_REGEXP);
			var matchs2 = dateString.match(/(\d)+/g);
			// if(matchs1.length == matchs2.length) {
				var _date = new Date(1970, 0, 1);
				for(var i = 0; i < matchs1.length; i++) {
					var _int = parseInt(matchs2[i]);
					var sign = matchs1[i];
					switch(sign.charAt(0)) {
						case 'y':
							_date.setFullYear(_int);
							break;
						case 'M':
							_date.setMonth(_int - 1);
							break;
						case 'd':
							_date.setDate(_int);
							break;
						case 'h':
							_date.setHours(_int);
							break;
						case 'm':
							_date.setMinutes(_int);
							break;
						case 's':
							_date.setSeconds(_int);
							break;
					}
				}
				return _date;
			// }
			// return null;
		}

	},
	//字段超长省略
	showMaxParam: function(maxLength) {
		var hideText = document.querySelectorAll('.text-hide');
		var showText = document.querySelectorAll('.text-show');
		//预定的字符串
		//				var maxLength = 40;
		for(var ins = 0; ins < hideText.length; ins++) {
			hideText[ins].index = ins;
			var inner = hideText[ins].innerHTML;
			//去空格两两之间只留有一个空格
			var str = inner.split('');
			for(var i = 0; i < str.length; i++) {
				if(str[i] == ' ') {
					str.splice(i, 1);
				}
			}
			//去完空格之后的string
			var newStr = str.join('');
			var len = newStr.length;
			//大于保留的字符时显示查看更多或...
			if(len >= maxLength) {
				showText[ins].innerHTML = newStr.slice(0, maxLength) + "...";
				hideText[ins].style.display = 'none';
			} else {
				showText[ins].style.display = 'none';
//				hideText[ins].innerHTML = newStr.slice(0, maxLength);
			}
		}
		setTimeout(() => {
			var curHideText = document.querySelectorAll('.text-hide');
			if(curHideText.length != hideText.length) {
				this.showMaxParam(maxLength);
			}
		}, 0);
	},

};
