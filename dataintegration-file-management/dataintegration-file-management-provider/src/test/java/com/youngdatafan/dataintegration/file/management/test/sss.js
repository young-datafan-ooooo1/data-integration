"use strict";

function _toConsumableArray(e) {
    if (Array.isArray(e)) {
        for (var t = 0, n = Array(e.length); t < e.length; t++) n[t] = e[t];
        return n
    }
    return Array.from(e)
}

function _defineProperty(e, t, n) {
    return t in e ? Object.defineProperty(e, t, {
        value: n,
        enumerable: !0,
        configurable: !0,
        writable: !0
    }) : e[t] = n, e
}

function _defineProperty(e, t, n) {
    return t in e ? Object.defineProperty(e, t, {
        value: n,
        enumerable: !0,
        configurable: !0,
        writable: !0
    }) : e[t] = n, e
}

var _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
    return typeof e
} : function (e) {
    return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
};
self === top ? document.body.style.display = "block" : top.location = self.location, "addEventListener" in document && document.addEventListener("DOMContentLoaded", function () {
    "undefined" != typeof FastClick && FastClick.attach(document.body)
}, !1), FastClick.prototype.focus = function (e) {
    var t = void 0;
    e.setSelectionRange && 0 !== e.type.indexOf("date") && "time" !== e.type && "month" !== e.type ? (t = e.value.length, e.focus(), e.setSelectionRange(t, t)) : e.focus()
};
var wbTmpl = angular.module("wbTmpl", []),
    app = angular.module("app", ["ngRoute", "wbTmpl", "feBase", "infinite-scroll", "monospaced.qrcode"]);
app.run(["$rootScope", "$http", "$location", "browserService", "feEnvService", "feWebService", "fePageService", "weixinService", "CONSTANT", "feUtils", "feCache", "feActionSheet", "feAnalyticsService", "feConfirm", "feAlert", "fePwd", "feLoading", "feQRCode", "feTips", "demandLoadingService", "envConfig", "commonConfig", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h, C, b, y) {
    function w() {
        "object" === ("undefined" == typeof wx ? "undefined" : _typeof(wx)) && wx.hideAllNonBaseMenuItem()
    }

    function S() {
        var e = document.getElementsByClassName("fe-confirm");
        e && e.length && m.hide();
        var t = document.getElementsByClassName("fe-alert");
        t && t.length && f.hide();
        var n = document.getElementsByClassName("mod-actionsheet");
        n && n.length && d.hide();
        var a = document.getElementsByClassName("fe-pwd");
        a && a.length && g.hide();
        var i = document.getElementsByClassName("fe-loading");
        i && i.length && _.hide();
        var o = document.getElementsByClassName("gearDate");
        o && o.length && (gearDate.outerHTML = "");
        var r = document.getElementsByClassName("mod-tips");
        r && r.length && h.hide();
        var s = document.getElementsByClassName("qr-code");
        s && s.length && v.hide()
    }

    function E() {
        var t = e.analytics;
        if (!(t.routeChangeStartTime && t.routeChangeEndTime && t.routePath)) return void (t.routeChangeStartTime = (new Date).getTime());
        t.routeChangeStartTime = (new Date).getTime();
        var n = t.routeChangeStartTime - t.routeChangeEndTime;
        /^\/\w+\/?(\/\w+)?$/.test(t.routePath) && p.pageStayTime(n, t.routePath)
    }

    i.initEnv(b), e.hotline = c.APP.HOTLINE, e.analytics = {}, e.$on("$routeChangeStart", function (e, t) {
        var a = t && t.$$route;
        if (a && a.demandLoadArr && C.load(a.demandLoadArr), "all" !== a.openType && !l.isWechat()) return u.uc.extend(c.CACHE.COMMON_MSG, {
            title: "提示",
            content: y.msg.userWeixinBrowser
        }), void n.path("/common/msg");
        E();
        var a = t && t.$$route;
        a && S()
    }), e.$on("$routeChangeSuccess", function (t, n, i, o) {
        var s = n && n.$$route;
        s && (s.pageTitle && r.setPageTitle(s.pageTitle), a.filter(s), e.analytics.routePath = s.originalPath), w(), e.analytics.routeChangeEndTime = (new Date).getTime()
    }), document.onreadystatechange = function () {
        var e = document.readyState, t = window.indexLoadStartTime;
        if (t && "complete" === e) {
            var n = (new Date).getTime();
            p.bpRouteChange("index.html", {loadTime: n - t})
        }
    }, window.onbeforeunload = E
}]), app.factory("$exceptionHandler", ["$log", "feAnalyticsService", function (e, t) {
    return function (n, a) {
        e.warn(n, a), t.errorReport(n)
    }
}]), app.controller("historyListController", ["$scope", "$rootScope", "$location", "CONSTANT", "PageList", "apiConfig", "feUI", function (e, t, n, a, i, o, r) {
    e.historyList = new i({
        requestParam: o.activity.getHistoricalActivities, transformResponse: function (t) {
            return t && t.list && t.list.length ? e.notEmpty = !0 : e.empty = new r.EmptyList({
                isEmpty: !0,
                title: "暂未发布任何活动",
                iconClass: "ui-icon-banner-empty"
            }), t.list
        }, errorCallback: function () {
        }
    }), e.goHistoryDetail = function (e) {
        return e.activity_status == a.APP.MGM_ACTIVITY_STATUS.CLOSED ? void n.path("/mgm/noActivity") : void n.path("/mgm/home").search({activityId: e.activity_id})
    }
}]), app.controller("homeController", ["$scope", "$rootScope", "$timeout", "$interval", "$routeParams", "feLoading", "$location", "feTips", "feUtils", "$sce", "feCookie", "feAnalyticsService", "CONSTANT", "feCONSTANT", "feUI", "apiConfig", "commonConfig", "feQRCode", "marketingService", "feCache", "feWebService", "commonService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h, C, b, y) {
    function w() {
        e.activityInfo = {
            showHomeUpBtn: !0,
            isEnd: !1,
            goInvitedListPage: function () {
                d.bpButtonClick("mgm_home_goInvitedListBtn"), r.path("/mgm/invitedList")
            },
            goExchangedListPage: function () {
                d.bpButtonClick("mgm_home_goExchangeListBtn"), r.path("/mgm/exchangedList").search({activityId: i.activityId})
            },
            goGiftExchangePage: function () {
                d.bpButtonClick("mgm_home_goGiftExchangePageBtn"), r.path("/mgm/giftExchange").search({activityId: i.activityId})
            },
            showAwardCoinsDesc: function () {
                s.show({title: "奖励金", content: "获得奖励金后可在微业伴活动页兑换等值奖品，若已为企业金融老用户则不发放奖励金（完成过认证授权即为老客户）。"})
            },
            nickname: u.get(_.cache.cookie.nickname.key) || "",
            headimgurl: u.get(_.cache.cookie.headimgurl.key) || ""
        }, e.goHistoryPage = function () {
            d.bpButtonClick("mgm_home_goHistoryPageBtn"), r.path("/mgm/historyList")
        }, e.inviteBtn = new f.Button({
            text: "立即邀请", waValue: "mgm_home_inviteBtn", disabled: !1, click: function () {
                u.set(T.nickname.key, e.activityInfo.nickname, {path: T.nickname.path, secure: A}), h.showMgmShareMenu()
            }
        }), e.showRulesDialog = function () {
            e.activityInfo.showRules = !0
        }
    }

    function S(t) {
        u.set(T.agree_register_award_coins.key, t.cover_inviter_award_coins, {
            path: T.agree_register_award_coins.path,
            secure: A
        }), u.set(T.mgm_activity_id.key, t.activity_id, {
            path: T.mgm_activity_id.path,
            secure: A
        }), e.activityInfo = angular.extend(e.activityInfo, t);
        var n = {data: {activity_id: E, pageNumber: 1, pageSize: m.PAGE.PAGESIZE}};
        e.activityInfo.activity_rule_desc = l.trustAsHtml(e.activityInfo.activity_rule_desc.replace(/\\n/g, "<br>")), e.activityInfo.award_coins_expire_time = c.formatDate(e.activityInfo.award_coins_expire_time), b.http(angular.extend(n, g.gift.getAvailableAwards)).then(function (t) {
            var n = t.data.list || [];
            n.length > 3 && (n = n.splice(0, 3)), e.activityInfo.imgList = n
        })
    }

    w();
    var E = i.activityId, T = _.cache.cookie, A = y.getCookieSecure();
    if (E) {
        var N = {data: {activity_id: E}};
        b.http(angular.extend(N, g.activity.getActivityInfo)).then(function (t) {
            var n = t.data;
            n.is_end == p.APP.ACTIVITY_IS_END.IS_END && (e.activityInfo.isEnd = !0, e.inviteBtn.disabled = !0), S(n)
        }, function (t) {
            e.inviteBtn.disabled = !0, e.activityInfo.isNetWorkErr = !0
        })
    } else y.goErrorMsg({}, !0)
}]), app.controller("invitedListController", ["$scope", "$location", "commonConfig", "feCookie", "$routeParams", "feUI", "apiConfig", "feUtils", "CONSTANT", "PageList", "feQRCode", "marketingService", function (e, t, n, a, i, o, r, s, c, l, u, d) {
    var p = n.cache.cookie, m = a.get(p.mgm_activity_id.key);
    angular.extend(r.activity.getInvitedCorps, {data: {activity_id: m}}), e.isEnd = !1, e.invitedList = new l({
        requestParam: r.activity.getInvitedCorps,
        transformResponse: function (t) {
            t && t.list && t.list.length ? (e.invitedSize = t.total, e.notEmpty = !0) : e.empty = new o.EmptyList({
                isEmpty: !0,
                title: "当前没有邀请记录",
                iconClass: " ",
                desc: "快去邀请小伙伴们加入<br>即可获得奖励金兑换奖品"
            }), t.is_end && "Y" === t.is_end && (e.isEnd = !0);
            var n = !0, a = !1, i = void 0;
            try {
                for (var r, c = (t.list || [])[Symbol.iterator](); !(n = (r = c.next()).done); n = !0) {
                    var l = r.value;
                    l.invite_enterprise_time = s.formatDate(l.invite_enterprise_time)
                }
            } catch (e) {
                a = !0, i = e
            } finally {
                try {
                    !n && c.return && c.return()
                } finally {
                    if (a) throw i
                }
            }
            return t.list
        }
    }), e.inviteBtn = new o.Button({
        text: "立即邀请", waValue: "mgm_invitedList_inviteBtn", click: function () {
            d.showMgmShareMenu()
        }
    })
}]), app.controller("noActivityController", ["$scope", function (e) {
}]), app.controller("manageAddressController", ["$scope", "$location", "CONSTANT", "apiConfig", "PageList", "feCache", "feUI", "feConfirm", "feWebService", "exchangeDetailService", function (e, t, n, a, i, o, r, s, c, l) {
    var u = o.uc.get(n.CACHE.ADDRESS);
    e.button = {
        addAddress: new r.Button({
            text: "新增地址", click: function () {
                o.uc.remove(n.CACHE.ADDRESS), t.path("/address/editAddress").search({type: 1})
            }
        }), confirm: new r.Button({
            text: "确定", click: function () {
                l.setChooseAddress(u), o.uc.extend(n.CACHE.ADDRESS, u), t.path("/mgm/exchangeDetail")
            }
        })
    }, e.check = function (t) {
        u && (u.checked = !1), e.addressList.items[t].checked = !0, u = e.addressList.items[t]
    }, e.delete = function (t) {
        s.show({
            content: "确定删除该地址？", btn: {
                primary: {
                    text: "确定", click: function () {
                        s.hide();
                        var i = {data: {address_id: e.addressList.items[t].address_id}};
                        c.http(angular.extend(i, a.address.delAddress)).then(function () {
                            e.addressList.items[t].address_id == u.address_id && (u = null);
                            var a = l.getChooseAddress();
                            null != a && a.address_id && l.setChooseAddress(null), o.uc.extend(n.CACHE.ADDRESS, u), e.addressList.refresh()
                        })
                    }
                }
            }
        })
    }, e.edit = function (a) {
        o.uc.extend(n.CACHE.ADDRESS, e.addressList.items[a]), t.path("/address/editAddress")
    };
    var d = function () {
        e.addressList = new i({
            requestParam: a.address.getAddressList, pageSize: 5, transformResponse: function (t) {
                null == t.list || 0 == t.list.length ? (e.button.confirm.show = !1, e.empty = new r.EmptyList({
                    isEmpty: !0,
                    title: "地址列表为空",
                    iconClass: "ui-icon-banner-empty"
                })) : e.notEmpty = !0;
                var n = !0, a = !1, i = void 0;
                try {
                    for (var o, s = (t.list || [])[Symbol.iterator](); !(n = (o = s.next()).done); n = !0) {
                        var c = o.value;
                        u ? c.address_id == u.address_id ? (u = c, c.checked = !0) : c.checked = !1 : (u = c, c.checked = !0)
                    }
                } catch (e) {
                    a = !0, i = e
                } finally {
                    try {
                        !n && s.return && s.return()
                    } finally {
                        if (a) throw i
                    }
                }
                return t.list
            }
        })
    };
    d()
}]), app.controller("editAddressController", ["$scope", "$http", "$location", "$routeParams", "CONSTANT", "apiConfig", "feCache", "feUI", "feToast", "feAreaPicker", "feWebService", "fePageService", function (e, t, n, a, i, o, r, s, c, l, u, d) {
    var p = r.uc.get(i.CACHE.ADDRESS) || {}, m = function (e) {
        1 == e && d.setPageTitle("增添地址")
    }, f = " ", g = a.type;
    m(g);
    var _, v = [p.province_name || "选择省", p.city_name || "选择市", p.district_name || (p.city_name ? "" : "选择区")];
    u.http(angular.extend({}, o.api.province)).then(function (e) {
        _ = new l.AreaPicker({
            data: e,
            valSeparator: f,
            selectedData: [{areaname: v[0], areacode: p.province_code || null}, {
                areaname: v[1],
                areacode: p.city_code || null
            }, {areaname: v[2], areacode: p.district_code || null}],
            dataMap: {text: "areaname", value: "areacode", subs: "subarea"}
        })
    }), e.input = {
        receiverName: new s.Input({
            lab: "收件人",
            val: p.recipient || "",
            placeholder: "姓名",
            inputClass: "black-light flex-1 ft-14",
            labelClass: "blue form-group-title",
            itemClass: "form-group flex ft-14",
            maxlength: 50,
            validation: {
                validator: function (e) {
                    return "" != e && "" != e.trim()
                }, err: "收件人不能为空"
            }
        }),
        receiverMobile: new s.InputMobile({
            lab: "联系电话",
            val: p.recipient_phone || "",
            placeholder: "收件人联系电话",
            inputClass: "black-light flex-1 ft-14",
            labelClass: "blue form-group-title",
            itemClass: "form-group flex ft-14"
        }),
        location: new s.Input({
            lab: "所在地区",
            val: v.join(" "),
            placeholder: "请选择所在地区",
            inputClass: "black-light flex-1 ft-14",
            labelClass: "blue form-group-title",
            itemClass: "form-group flex ft-14",
            readonly: !0,
            click: function () {
                _.show(e.input.location)
            }
        }),
        receiverAddress: new s.Input({
            lab: "详细街道",
            val: p.street_detail || "",
            placeholder: "街道门牌信息",
            inputClass: "black-light flex-1 ft-14",
            labelClass: "blue form-group-title",
            itemClass: "form-group flex ft-14 bb0",
            validation: {
                validator: function (e) {
                    return !!e
                }, err: "输入街道信息有误"
            },
            maxlength: 100
        })
    };
    var h = function () {
        var t = !0;
        for (var n in e.input) if (e.input.hasOwnProperty(n) && (e.input[n].validate(), !e.input[n].isValid)) {
            t = !1;
            break
        }
        return t
    }, C = function () {
        var t = e.input.location.val;
        return !!t && "选择省 选择市 选择区" != t
    };
    e.button = new s.Button({
        text: "确定", click: function () {
            if (h()) if (C()) {
                var t = _.options.selectedData, a = t[0] || {}, s = t[1] || {}, l = t[2] || {}, d = {
                    data: {
                        recipient: e.input.receiverName.val,
                        recipient_phone: e.input.receiverMobile.val,
                        province_code: a.areacode,
                        province_name: a.areaname,
                        city_code: s.areacode,
                        city_name: s.areaname,
                        district_code: l.areacode,
                        district_name: l.areaname,
                        street_detail: e.input.receiverAddress.val
                    }
                }, m = !!r.uc.get(i.CACHE.ADDRESS);
                m && (d.data = angular.extend(d.data, {address_id: p.address_id})), u.http(angular.extend(d, m ? o.address.editAddress : o.address.addAddress)).then(function () {
                    n.path("/address/manageAddress")
                })
            } else c.show("请选择所在地区")
        }
    })
}]), app.controller("advertisement", ["$scope", "$location", "$routeParams", "feWebService", "apiConfig", "commonService", "feUtils", "feUI", "feEnvService", "envConfig", "$http", "feToast", "commonConfig", function (e, t, n, a, i, o, r, s, c, l, u, d, p) {
    function m(e) {
        return /\d{1,5}/.test(e)
    }

    function f() {
        var t = !0;
        for (var n in e.input) if (e.input.hasOwnProperty(n) && (e.input[n].validate(), !e.input[n].isValid)) {
            t = !1;
            break
        }
        return t
    }

    function g(t) {
        var n = e.channelId, a = ["8", "9", "11", "12"];
        if (t && e.requestId && a.indexOf(n) !== -1) {
            var i = e.requestId,
                o = l.aiSubmitUrl + "?requestId=" + i + "&status=" + t + "&channel_id=" + n + "&callback=JSON_CALLBACK";
            return void u.jsonp(o).success(function (e) {
            })
        }
    }

    function _() {
        var t = e.input.receiverMobile.val;
        return r.isValidPhoneNumber(t) ? (e.btn.otp.countdown(), void a.http(angular.extend({
            data: {
                phone_no: t,
                advert_id: e.advertId
            }
        }, i.common.getOtp)).then(function (e) {
        })) : void d.show(p.msg.mobileNumError)
    }

    e.area = n.area || "440001", e.advertId = n.advert_id || "", e.channelId = n.channel_id || r.getParamFromURL("channel_id"), e.portal = n.portal || "", e.clickId = n.gdt_vid || r.getParamFromURL("gdt_vid"), e.requestId = n.requestId, e.url = t.absUrl(), e.show = !1, e.submitSuccess = !1, e.existsNum = "10000", e.makeSure = "", e.showRules = !0, "02" === e.channelId && (e.clickId = n.qz_gdt || r.getParamFromURL("qz_gdt"));
    var v = {1: "success", 2: "error", 3: "warning"};
    if (e.mod = {
        show: function (t, n, a) {
            e.submitSuccess = t, e.iconType = "content-icon--" + v[n], e.submitTips = a, e.show = !0
        }, close: function () {
            e.show = !1
        }
    }, e.consumerHotline = "4009998866", m(e.advertId)) {
        var h = {data: {advert_id: e.advertId}};
        a.http(angular.extend(h, i.advertisement.getExistsNum)).then(function (t) {
            var n = t.data;
            n.hasOwnProperty("exists_num") && (e.existsNum = n.exists_num)
        })
    }
    var C = "";
    if (e.portal) C = c.getApiPrefix(), e.qrcodeHash = C + i.advertisement.qryChannelCode.url + "?portal=" + e.portal; else {
        C = r.getCDNUrl() ? r.getCDNUrl() : "./";
        var b = {
            440001: C + "asset/images/ad/ad_qrcode_GuangDong.png",
            320000: C + "asset/images/ad/ad_qrcode_JiangSu.png",
            440300: C + "asset/images/ad/ad_qrcode_ShenZhen.png",
            430000: C + "asset/images/ad/ad_qrcode_HuNan.jpg",
            610000: C + "asset/images/ad/ad_qrcode_ShanXi.png",
            330000: C + "asset/images/ad/ad_qrcode_ZheJiang.png"
        };
        e.qrcodeHash = b[e.area]
    }
    "440300" === e.area ? e.isXWD = !0 : e.isXWD = !1, e.input = {
        enterprisName: new s.Input({
            lab: "",
            val: "",
            placeholder: "请输入企业名称",
            inputClass: "info-input",
            labelClass: "title",
            itemClass: "consult-info__item",
            maxlength: 50,
            validation: {
                validator: function (e) {
                    return "" != e && "" != e.trim()
                }, err: "企业名称不能为空"
            }
        }),
        contactName: new s.Input({
            lab: "",
            val: "",
            placeholder: "请输入联系人",
            inputClass: "info-input",
            labelClass: "title title--diff",
            itemClass: "consult-info__item",
            maxlength: 50,
            validation: {
                validator: function (e) {
                    return "" != e && "" != e.trim()
                }, err: "联系人不能为空"
            }
        }),
        receiverMobile: new s.InputMobile({
            lab: " ",
            val: "",
            placeholder: "请输入手机号码",
            inputClass: "info-input",
            labelClass: "title",
            itemClass: "consult-info__item"
        }),
        otp: new s.InputOTP({
            placeholder: "请输入验证码",
            inputClass: "info-input",
            labelClass: "title title--hide",
            itemClass: "consult-info__item",
            change: function () {
                this.validate()
            }
        })
    }, g("0"), e.btn = {
        otp: new s.ButtonOTP({
            click: function () {
                return !this.disabled() && void _()
            }
        }), submit: function () {
            if (!e.advertId || !e.channelId) return e.mod.show(!1, "2", "留资ID或渠道号获取不到！"), void (e.makeSure = "返回");
            if (e.repeated = !0, f()) {
                var t = {
                    data: {
                        enterprise_name: e.input.enterprisName.val,
                        contact_name: e.input.contactName.val,
                        contact_phone_no: e.input.receiverMobile.val,
                        channel_id: e.channelId,
                        advert_id: e.advertId,
                        click_id: e.clickId,
                        url: e.url,
                        request_id: e.requestId,
                        otp_code: e.input.otp.val
                    }
                };
                a.http(angular.extend(t, i.advertisement.submitConsumerInfo)).then(function (t) {
                    0 === t.status && "SUCCESSED" === t.msg && (e.mod.show(!0, "1", "信息提交成功！"), e.makeSure = "确定", e.repeated = !1)
                }, function (t) {
                    e.mod.show(!1, "3", t.msg), e.makeSure = "确定", e.repeated = !1
                }), g("1")
            } else e.mod.show(!1, "2", "您输入的信息有误，请查正修改。"), e.makeSure = "返回", e.repeated = !1
        }
    }
}]), app.controller("brandName", ["$scope", "$location", "$routeParams", "feWebService", "apiConfig", "commonService", "feUtils", "feUI", "feLoading", "feEnvService", "envConfig", "$http", "commonConfig", "feCookie", "feCache", "feSession", "CONSTANT", "feToast", "$timeout", "$sce", "weixinService", "marketingService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h, C, b, y) {
    var w = p.cache.cookie, S = m.get(w.advert_id.key), E = f.uc.get(_.CACHE.BRAND_NAME) || {},
        T = {data: {advert_id: S}}, A = "asset" === n.page_type;
    a.http(angular.extend(T, i.advertisement.getExistsNum)).then(function (t) {
        t && (e.bransList = t.data.brandArry, e.filterBransList = e.bransList)
    });
    var N = "asset/images/empty-tip.png";
    e.emptyPic = r.getCDNUrl() ? r.getCDNUrl() + N : "./" + N, e.ord = {
        obtian: function (e) {
            m.set(w.brand_name.key, e.target.innerText);
            A ? t.path("/leavePage/leaveStaticforDHD") : t.path("/leaveMessage"), E && f.uc.extend(_.CACHE.ADVERTISEMENT, E)
        }
    }, e.input = {
        brands: new s.Input({
            lab: "",
            val: "",
            placeholder: "输入关键词查找",
            inputClass: "info-input",
            labelClass: "ui-icon-search",
            itemClass: "brands_input",
            change: function () {
                var t = e.input.brands.val;
                e.filterBransList = e.bransList.filter(function (e) {
                    return e.brand_name.indexOf(t) > -1
                })
            }
        })
    }
}]), app.controller("leaveMessage", ["$scope", "$location", "$routeParams", "feWebService", "apiConfig", "commonService", "feUtils", "feUI", "feLoading", "feEnvService", "envConfig", "$http", "commonConfig", "feCookie", "feCache", "feSession", "CONSTANT", "feToast", "$timeout", "$sce", "weixinService", "marketingService", "feAnalyticsService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h, C, b, y, w) {
    function S() {
        var t = e.ordShow ? e.input.receiverMobileOrd.val : e.input.receiverMobile.val;
        return r.isValidPhoneNumber(t) ? (e.ordShow ? e.btnOrd.otp.countdown() : e.btn.otp.countdown(), void a.http(angular.extend({
            data: {
                phone_no: t,
                advert_id: e.advertId
            }
        }, i.common.getOtp)).then(function (e) {
        })) : void v.show(p.msg.mobileNumError)
    }

    function E(e) {
        return /\d{1,5}/.test(e)
    }

    function T(t, n) {
        if (e.product_cd = t.product_cd, t) {
            t.product_cd ? "企业爱普app微信登录态下载模板" === t.template_name ? (k("qyAppLoadShow"), $(t)) : "有企业名称的app下载模板页面" === t.template_name ? (k("qyAppHasNameShow"), M(t)) : "订货贷" === t.product_cd ? (e.ordShow = !0, e.entShow = !1, e.oldShow = !1, e.appLoadShow = !1, U(t), w.bpRouteChange("dhd_leaveMessage_enter")) : "APP下载" === t.product_cd ? (e.appLoadShow = !0, e.ordShow = !1, e.entShow = !1, e.oldShow = !1, R(t)) : "供货贷" === t.product_cd ? (k("ghdShow"), W(t)) : "订货贷" !== t.product_cd && (e.entShow = !0, e.ordShow = !1, e.oldShow = !1, e.appLoadShow = !1, D(t)) : (a.http(angular.extend(n, i.advertisement.getExistsNum)).then(function (t) {
                var n = t.data;
                n.hasOwnProperty("exists_num") && (e.existsNum = n.exists_num)
            }), e.oldShow = !0, e.entShow = !1, e.ordShow = !1, O());
            var o = t.open_in_wechat;
            if ("Y" === o) {
                c.show(!0);
                var s = r.getParamFromURL("code");
                if (!s) return g.set(V, JSON.stringify(t)), void (window.location.href = b.getWeChatOAuthUrl(window.location.href));
                c.hide();
                var l = g.get(p.cache.session.ad_open_id.key), u = g.get(p.cache.session.ad_union_id.key);
                if (!u && !l) {
                    var d = {data: {code: s, client_sys_info: r.getClientSysInfo()}};
                    y.commonLogin(d)
                }
            }
        } else a.http(angular.extend(n, i.advertisement.getExistsNum)).then(function (t) {
            var n = t.data;
            n.hasOwnProperty("exists_num") && (e.existsNum = n.exists_num)
        }), e.oldShow = !0, e.entShow = !1, e.ordShow = !1, O()
    }

    function A() {
        var t = !0;
        for (var n in e.input) if (e.input.hasOwnProperty(n) && e.input[n].validate && (e.input[n].validate(), !e.input[n].isValid)) {
            t = !1;
            break
        }
        return t
    }

    function N(t) {
        var n = e.channelId, a = ["8", "9", "11", "12"];
        if (t && e.requestId && a.indexOf(n) !== -1) {
            var i = e.requestId,
                o = u.aiSubmitUrl + "?requestId=" + i + "&status=" + t + "&channel_id=" + n + "&callback=JSON_CALLBACK";
            return void d.jsonp(o).success(function (e) {
            })
        }
    }

    function I() {
        if ("Y" === e.previewUrl) {
            if ("2" === e.nameCode) return e.modEnt.showEnt(!1, "2", "预览界面不能提交留资信息！"), e.makeSure = "返回", e.input.enterprisName.val = "", e.input.contactName.val = "", void (e.input.receiverMobile.val = "");
            if ("3" === e.nameCode) return e.modOrd.showOrd(!1, "2", "预览界面不能提交留资信息！"), e.makeSure = "返回", e.input.brandName.val = "", e.input.enterprisNameOrd.val = "", e.input.contactNameOrd.val = "", e.input.receiverMobileOrd.val = "", void (e.input.agentCode.val = "");
            if ("4" === e.nameCode) return e.modOrd.showOrd(!1, "2", "预览界面不能提交留资信息！"), e.makeSure = "返回", e.input.receiverMobile.val = "", void (e.input.otp.val = "");
            if (e.nameCode === F.HAS_ENTERPRISE_APP_DOWNLOAD) return e.modOrd.showOrd(!1, "2", "预览界面不能提交留资信息！"), e.makeSure = "返回", e.input.receiverMobile.val = "", e.input.otp.val = "", void (e.input.enterprisName.val = "");
            if ("6" === e.nameCode) return e.commonDialog.showCommonDialog(!1, "2", "预览界面不能提交留资信息！"), e.makeSure = "返回", void e.resetValues()
        } else {
            if (!e.advertId || !e.channelId) return "1" === e.nameCode ? e.mod.show(!1, "2", "留资ID或渠道号获取不到！") : "2" === e.nameCode ? e.modEnt.showEnt(!1, "2", "留资ID或渠道号获取不到！") : "3" === e.nameCode ? e.modOrd.showOrd(!1, "2", "留资ID或渠道号获取不到！") : "4" === e.nameCode ? e.appLoad.showOrd(!1, "2", "留资ID或渠道号获取不到！") : "6" === e.nameCode && e.commonDialog.showCommonDialog(!1, "2", "留资ID或渠道号获取不到！"), void (e.makeSure = "返回");
            if (e.repeated = !0, A()) {
                var t = {};
                "3" === e.nameCode ? (t = {
                    data: {
                        brand_name: e.input.brandName.val,
                        enterprise_name: e.input.enterprisNameOrd.val,
                        contact_name: e.input.contactNameOrd.val,
                        contact_phone_no: e.input.receiverMobileOrd.val,
                        agent_code: e.input.agentCode.val,
                        channel_id: e.channelId,
                        advert_id: e.advertId,
                        click_id: e.clickId,
                        url: e.url,
                        request_id: e.requestId,
                        otp_code: e.input.otp.val
                    }
                }, w.bpButtonClick("dhd_leaveMessage_submit")) : "4" === e.nameCode ? t = {
                    data: {
                        contact_phone_no: e.input.receiverMobile.val,
                        channel_id: e.channelId,
                        advert_id: e.advertId,
                        click_id: e.clickId,
                        url: e.url,
                        request_id: e.requestId,
                        otp_code: e.input.otp.val
                    }
                } : e.nameCode === F.HAS_ENTERPRISE_APP_DOWNLOAD ? (t = {
                    data: {
                        enterprise_name: e.input.enterprisName.val,
                        contact_phone_no: e.input.receiverMobile.val,
                        channel_id: e.channelId,
                        advert_id: e.advertId,
                        click_id: e.clickId,
                        url: e.url,
                        request_id: e.requestId,
                        otp_code: e.input.otp.val
                    }
                }, q("buttonClick", "has_enterprise_name_download", {})) : t = "6" === e.nameCode ? {
                    data: {
                        enterprise_name: e.input.enterprisName.val,
                        contact_name: e.input.contactName.val,
                        contact_phone_no: e.input.receiverMobile.val,
                        core_enterprise_name: e.input.coreEnterpriseName.val,
                        channel_id: e.channelId,
                        advert_id: e.advertId,
                        click_id: e.clickId,
                        url: e.url,
                        request_id: e.requestId,
                        otp_code: e.input.otp.val
                    }
                } : "2" !== e.nameCode || e.isAdvertShow ? {
                    data: {
                        enterprise_name: e.input.enterprisName.val,
                        contact_name: e.input.contactName.val,
                        contact_phone_no: e.input.receiverMobile.val,
                        channel_id: e.channelId,
                        advert_id: e.advertId,
                        click_id: e.clickId,
                        url: e.url,
                        request_id: e.requestId,
                        otp_code: e.input.otp.val
                    }
                } : {
                    data: {
                        contact_phone_no: e.input.receiverMobile.val,
                        channel_id: e.channelId,
                        advert_id: e.advertId,
                        click_id: e.clickId,
                        url: e.url,
                        request_id: e.requestId,
                        otp_code: e.input.otp.val
                    }
                }, t.data = Object.assign(r.getParamFromURL(), n, t.data), t.data.efs_channel_id = e.efs_channel_id, a.http(angular.extend(t, i.advertisement.submitConsumerInfo)).then(function (t) {
                    0 === t.status && "SUCCESSED" === t.msg && ("1" === e.nameCode ? e.mod.show(!0, "1", "信息提交成功！") : "2" === e.nameCode ? (e.modEnt.showEnt(!0, "1", "信息提交成功！"), e.imgShow = !0, e.input.enterprisName.val = "", e.input.contactName.val = "", e.input.receiverMobile.val = "") : "3" === e.nameCode ? (e.modOrd.showOrd(!0, "1", "信息提交成功！"), e.imgShow = !0, e.input.brandName.val = "", e.input.enterprisNameOrd.val = "", e.input.contactNameOrd.val = "", e.input.receiverMobileOrd.val = "", e.input.agentCode.val = "") : "4" === e.nameCode ? (e.appLoad.showOrd(!0, "1", "信息提交成功！"), e.imgShow = !0, e.input.receiverMobile.val = "", e.input.otp.val = "", x(e.button_ios, e.button_android)) : e.nameCode === F.HAS_ENTERPRISE_APP_DOWNLOAD ? (e.appLoad.showOrd(!0, "1", "信息提交成功！"), e.imgShow = !0, e.input.receiverMobile.val = "", e.input.otp.val = "", e.input.enterprisName.val = "", x(e.button_ios, e.button_android)) : "6" === e.nameCode && (e.commonDialog.showCommonDialog(!0, "1", "信息提交成功！"), e.imgShow = !0, e.resetValues()), e.makeSure = "确定", e.repeated = !1)
                }, function (t) {
                    "1" === e.nameCode ? e.mod.show(!1, "3", t.msg) : "2" === e.nameCode ? e.modEnt.showEnt(!1, "3", t.msg) : "3" === e.nameCode ? e.modOrd.showOrd(!1, "3", t.msg) : "4" === e.nameCode ? e.appLoad.showOrd(!1, "3", t.msg) : "6" === e.nameCode ? e.commonDialog.showCommonDialog(!1, "3", t.msg) : e.nameCode === F.HAS_ENTERPRISE_APP_DOWNLOAD && e.appLoad.showOrd(!1, "3", t.msg), e.makeSure = "确定", e.repeated = !1
                }), N(1)
            } else "1" === e.nameCode ? e.mod.show(!1, "2", "您输入的信息有误，请查正修改。") : "2" === e.nameCode ? e.modEnt.showEnt(!1, "2", "您输入的信息有误，请查正修改。") : "3" === e.nameCode ? e.modOrd.showOrd(!1, "2", "您输入的信息有误，请查正修改。") : "4" === e.nameCode ? e.appLoad.showOrd(!1, "2", "您输入的信息有误，</br>请查证修改") : "6" === e.nameCode && e.commonDialog.showCommonDialog(!1, "2", "您输入的信息有误，</br>请查证修改"), e.makeSure = "返回", e.repeated = !1
        }
    }

    function x(e, t) {
        var n = Y ? t : e;
        n || (n = "https://sit.test.webankcdn.net/tcftp/cfa/cpmm.html"), window.location.href = n
    }

    function L() {
        document.body.scrollTop = 0
    }

    function P(e, t) {
        var n = e;
        if (!e && t) {
            var a = "./";
            r.getCDNUrl() && (a = r.getCDNUrl()), n = a + t
        }
        return n
    }

    function k(t) {
        e.appLoadShow = !1, e.ordShow = !1, e.entShow = !1, e.oldShow = !1, e.qyAppLoadShow = !1, e[t] = !0
    }

    function O() {
        e.nameCode = "1";
        var t = "";
        if (e.portal) t = l.getApiPrefix(), e.qrcodeHash = t + i.advertisement.qryChannelCode.url + "?portal=" + e.portal; else {
            t = r.getCDNUrl() ? r.getCDNUrl() : "./";
            var n = {
                440001: t + "asset/images/ad/ad_qrcode_GuangDong.png",
                320000: t + "asset/images/ad/ad_qrcode_JiangSu.png",
                440300: t + "asset/images/ad/ad_qrcode_ShenZhen.png",
                430000: t + "asset/images/ad/ad_qrcode_HuNan.jpg",
                610000: t + "asset/images/ad/ad_qrcode_ShanXi.png",
                330000: t + "asset/images/ad/ad_qrcode_ZheJiang.png"
            };
            e.qrcodeHash = n[e.area]
        }
        "440300" === e.area ? e.isXWD = !0 : e.isXWD = !1, e.input = {
            enterprisName: new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入企业名称",
                inputClass: "info-input",
                labelClass: "title",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return "" != e && "" != e.trim()
                    }, err: "企业名称不能为空"
                }
            }),
            contactName: new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入联系人",
                inputClass: "info-input",
                labelClass: "title title--diff",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return "" != e && "" != e.trim()
                    }, err: "联系人不能为空"
                }
            }),
            receiverMobile: new s.InputMobile({
                lab: " ",
                val: "",
                placeholder: "请输入手机号码",
                inputClass: "info-input",
                labelClass: "title",
                itemClass: "consult-info__item"
            }),
            otp: new s.InputOTP({
                placeholder: "请输入验证码",
                inputClass: "info-input",
                labelClass: "title title--hide",
                itemClass: "consult-info__item",
                change: function () {
                    this.validate()
                }
            })
        }, N(0), e.btn = {
            otp: new s.ButtonOTP({
                click: function () {
                    return !this.disabled() && void S()
                }
            }), submit: function () {
                I()
            }
        }
    }

    function D(t) {
        e.nameCode = "2", t.page_name ? document.title = t.page_name : document.title = "微业贷";
        var n = "";
        t.banner_one_url ? e.bannerOneUrl = t.banner_one_url : (n = r.getCDNUrl() ? r.getCDNUrl() : "./", e.bannerOneUrl = n + "asset/images/ad/ent_banner.jpg"), t.banner_two_url && "N" === t.show_banner_two ? e.bannerTwoUrl = t.banner_two_url : t.banner_two_url && "Y" === t.show_banner_two ? e.bannerTwoUrl = "" : t.banner_two_url || "Y" !== t.show_banner_two ? (n = r.getCDNUrl() ? r.getCDNUrl() : "./", e.bannerTwoUrl = n + "asset/images/ad/banner2.png") : e.bannerTwoUrl = "", t.banner_three_url && "N" === t.show_banner_three ? e.bannerThreeUrl = t.banner_three_url : t.banner_three_url && "Y" === t.show_banner_three ? e.bannerThreeUrl = "" : t.banner_three_url || "Y" !== t.show_banner_three ? (n = r.getCDNUrl() ? r.getCDNUrl() : "./", e.bannerThreeUrl = n + "asset/images/ad/banner_three _ent .png") : e.bannerThreeUrl = "", t.banner_four_url && "N" === t.show_banner_four ? e.bannerFourUrl = t.banner_four_url : t.banner_four_url && "Y" === t.show_banner_four ? e.bannerFourUrl = "" : t.banner_four_url || "Y" !== t.show_banner_four ? (n = r.getCDNUrl() ? r.getCDNUrl() : "./", e.bannerFourUrl = n + "asset/images/ad/banner_four _ent .png") : e.bannerFourUrl = "", t.model_two_title ? e.modelTwoTitle = t.model_two_title : e.modelTwoTitle = "产品申请咨询", t.button_one_text ? e.buttonOneText = t.button_one_text : e.buttonOneText = "立刻申请", t.button_one_color ? e.buttonOneColor = t.button_one_color : e.buttonOneColor = "#4386ff", t.text_one ? e.textOne = t.text_one : e.isTextOneShow = !0, t.text_two ? e.textTwo = t.text_two : e.isTextTwoShow = !0, t.text_three ? e.textThree = t.text_three : e.isTextThreeShow = !0, e.input = {
            receiverMobile: new s.InputMobile({
                lab: " ",
                val: "",
                placeholder: "请输入",
                containerClass: "outerLayer",
                inputClass: "box__input",
                labelClass: "title",
                itemClass: "consult-info__item",
                blur: function () {
                    L()
                }
            }),
            enterprisName: e.isAdvertShow ? new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入",
                containerClass: "outerLayer",
                inputClass: "box__input",
                labelClass: "title",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return "" != e && "" != e.trim()
                    }, err: "企业名称不能为空"
                },
                change: function () {
                    var t = e.input.enterprisName.val.length;
                    if (G && h.cancel(G), e.input.enterprisName.val || (e.isCompanyShow = !1), t > 0) {
                        var n = [], o = {data: {root: e.input.enterprisName.val}};
                        e.input.receiverMobile.val && r.isValidPhoneNumber(e.input.receiverMobile.val) && (G = h(function () {
                            a.http(angular.extend(o, i.advertisement.getAssociatedNames)).then(function (t) {
                                n = t.data.list, n.length > 0 && (e.selectList = n, e.isCompanyShow = !0)
                            })
                        }, 400)), e.onItemDown = function (e) {
                            e.preventDefault()
                        }, e.onItemClick = function (t) {
                            e.isCompanyShow = !1, e.input.enterprisName.val = n[t]
                        }
                    } else e.isCompanyShow = !1
                },
                blur: function () {
                    e.isCompanyShow = !1, L()
                }
            }) : {},
            contactName: e.isAdvertShow ? new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入",
                containerClass: "outerLayer",
                inputClass: "box__input",
                labelClass: "title title--diff",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return "" != e && "" != e.trim()
                    }, err: "联系人不能为空"
                },
                blur: function () {
                    L()
                }
            }) : {},
            otp: new s.InputOTP({
                placeholder: "请输入验证码",
                inputClass: "info-input",
                labelClass: "title title--hide",
                itemClass: "consult-info__item",
                change: function () {
                    this.validate()
                },
                blur: function () {
                    L()
                }
            })
        }, N(0), e.btn = {
            otp: new s.ButtonOTP({
                click: function () {
                    return !this.disabled() && void S()
                }
            }), submitEnt: function () {
                I()
            }
        }
    }

    function U(n) {
        e.nameCode = "3", n.page_name ? document.title = n.page_name : document.title = "订货贷";
        var o = "";
        n.banner_one_url ? e.bannerOneUrlOrd = n.banner_one_url : (o = r.getCDNUrl() ? r.getCDNUrl() : "./", e.bannerOneUrlOrd = o + "asset/images/ad/ord_banner.png");
        var c = "asset/images/ad/serive_icon.png";
        e.seriveTelOrd = r.getCDNUrl() ? r.getCDNUrl() + c : "./" + c, n.icon_one_url ? e.iconOneUrl = n.icon_one_url : (o = r.getCDNUrl() ? r.getCDNUrl() : "./", e.iconOneUrl = o + "asset/images/ad/icon_left.png"), n.icon_two_url ? e.iconTwoUrl = n.icon_two_url : (o = r.getCDNUrl() ? r.getCDNUrl() : "./", e.iconTwoUrl = o + "asset/images/ad/icon_right.png"), n.icon_text_one ? e.iconTextOne = n.icon_text_one : e.iconTextOne = "成立满半年公司或个体户", n.icon_text_two ? e.iconTextTwo = n.icon_text_two : e.iconTextTwo = "与品牌合作订货业务", n.text_one ? e.textOne = n.text_one : e.isTextOneShow = !0, e.consumerHotline = n.text_one ? n.text_one : "4009998866", n.text_two ? e.textTwo = n.text_two : e.isTextTwoShow = !0, n.text_three ? e.textThree = n.text_three : e.isTextThreeShow = !0;
        var l = p.cache.cookie;
        e.ord = {
            choice: function () {
                m.set(l.advert_id.key, e.advertId);
                f.uc.extend(_.CACHE.BRAND_NAME, {
                    enterprise_name: e.input.enterprisNameOrd.val,
                    contact_name: e.input.contactNameOrd.val,
                    contact_phone_no: e.input.receiverMobileOrd.val,
                    agent_code: e.input.agentCode.val
                }), t.path("/advertisement/brandName")
            }
        };
        var u = m.get(l.brand_name.key);
        e.input = {
            brandName: new s.Input({
                lab: "",
                val: u,
                placeholder: "请选择",
                containerClass: "outerLayerOrd",
                warningClass: "opt-warning",
                inputClass: "box__input",
                labelClass: "title",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return "" != e && "" != e.trim()
                    }, err: "所属品牌不能为空"
                },
                blur: function () {
                    L()
                }
            }),
            receiverMobileOrd: new s.InputMobile({
                lab: " ",
                val: "",
                placeholder: "请输入",
                containerClass: "outerLayer",
                warningClass: "opt-warning",
                inputClass: "box__input",
                labelClass: "title",
                itemClass: "consult-info__item",
                blur: function () {
                    L()
                }
            }),
            enterprisNameOrd: new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入营业执照上企业全称",
                containerClass: "outerLayer",
                inputClass: "info-enter box__input",
                warningClass: "opt-warning",
                labelClass: "title",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return void 0 != e && "" != e && "" != e.trim()
                    }, err: "企业名称不能为空"
                },
                change: function () {
                    var t = e.input.enterprisNameOrd.val.length;
                    if (G && h.cancel(G), e.input.enterprisNameOrd.val || (e.isCompanyShow = !1), t > 0) {
                        var n = [], o = {data: {root: e.input.enterprisNameOrd.val}};
                        e.input.receiverMobileOrd.val && r.isValidPhoneNumber(e.input.receiverMobileOrd.val) && (G = h(function () {
                            a.http(angular.extend(o, i.advertisement.getAssociatedNames)).then(function (t) {
                                n = t.data.list, n.length > 0 && (e.selectList = n, e.isCompanyShow = !0)
                            })
                        }, 400)), e.onItemDown = function (e) {
                            event.preventDefault()
                        }, e.onItemClick = function (t) {
                            e.isCompanyShow = !1, e.input.enterprisNameOrd.val = n[t]
                        }
                    } else e.isCompanyShow = !1
                },
                blur: function () {
                    e.isCompanyShow = !1, L()
                }
            }),
            contactNameOrd: new s.Input({
                lab: "",
                val: "",
                placeholder: "请填写",
                containerClass: "outerLayer",
                warningClass: "opt-warning",
                inputClass: "box__input",
                labelClass: "title title--diff",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return void 0 != e && "" != e && "" != e.trim();
                    }, err: "联系人不能为空"
                },
                blur: function () {
                    L()
                }
            }),
            agentCode: new s.Input({
                lab: "",
                val: "",
                placeholder: "选填",
                containerClass: "outerLayer",
                inputClass: "box__input",
                labelClass: "title title--diff",
                itemClass: "consult-info__item",
                warningClass: "opt-warning",
                maxlength: 50,
                blur: function () {
                    L()
                }
            }),
            otp: new s.InputOTP({
                lab: " ",
                placeholder: "请输入验证码",
                containerClass: "outerLayer",
                inputClass: "box__input",
                labelClass: "title title--hide",
                itemClass: "consult-info__item",
                warningClass: "opt-warning",
                change: function () {
                    this.validate()
                },
                blur: function () {
                    L()
                }
            })
        };
        var d = f.uc.get(_.CACHE.ADVERTISEMENT) || {};
        d && (e.input.enterprisNameOrd.val = d.enterprise_name, e.input.contactNameOrd.val = d.contact_name, e.input.receiverMobileOrd.val = d.contact_phone_no, e.input.agentCode.val = d.agent_code), N(0), e.btnOrd = {
            otp: new s.ButtonOTP({
                click: function () {
                    return !this.disabled() && void S()
                }
            }), submitOrd: function () {
                I()
            }
        }
    }

    function M(t) {
        r.setNonWritableKey(e, "nameCode", F.HAS_ENTERPRISE_APP_DOWNLOAD), R(t), e.input = Object.assign({
            enterprisName: new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入企业名称",
                inputClass: "info-input",
                hideLabel: !0,
                labelClass: "label-input",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return "" != e && "" != e.trim()
                    }, err: "企业名称不能为空"
                }
            })
        }, e.input), t.banner_three_url && (e.bannerThreeUrl = t.banner_three_url), t.banner_four_url && (e.bannerFourUrl = t.banner_four_url)
    }

    function R(t) {
        e.nameCode = "4", document.title = t.page_name || "APP下载";
        var n = "";
        t.banner_one_url ? e.bannerOneUrl = t.banner_one_url : (n = r.getCDNUrl() ? r.getCDNUrl() : "./", e.bannerOneUrl = n + "asset/images/ad/app-download-banner.jpg"), t.banner_two_url ? e.bannerTwoUrl = t.banner_two_url : (n = r.getCDNUrl() ? r.getCDNUrl() : "./", e.bannerTwoUrl = n + "asset/images/ad/app-download-banner2.jpg"), e.buttonOneText = t.button_one_text || "提交", e.buttonOneColor = t.button_one_color || "#4386ff", e.button_ios = t.button_ios, e.button_android = t.button_android, e.input = {
            receiverMobile: new s.InputMobile({
                lab: " ",
                val: "",
                placeholder: "请输入您的手机号",
                inputClass: "info-input",
                hideLabel: !0,
                labelClass: "label-input",
                blur: function () {
                    L()
                }
            }),
            otp: new s.Input({
                placeholder: "请输入验证码",
                inputClass: "otp-input",
                maxlength: 6,
                val: "",
                labelClass: "label-input",
                validation: {validator: r.isValidPhoneOtpNumber, err: "输入验证码有误"},
                change: function () {
                    this.validate()
                },
                blur: function () {
                    L()
                }
            })
        }, N(0), e.btn = {
            otp: new s.ButtonOTP({
                click: function () {
                    return !this.disabled() && void S()
                }
            }), submitOrd: function () {
                I()
            }
        }
    }

    function $(t) {
        var n = JSON.parse(g.get("register_info") || "{}").value || {}, a = n.phone_number || "";
        q("buttonClick", "qyapp_leaveMessage_enter", {phone_num: a});
        var i = "asset/images/ad/";
        e.nameCode = "5", document.title = t.page_name || "企业爱普APP下载", e.bannerOneUrl = P(t.banner_one_url, i + "qyapp_banner_1.jpg"), e.bannerTwoUrl = P(t.banner_two_url, i + "qyapp_banner_2.jpg"), e.bannerThreeUrl = P(t.banner_three_url, i + "qyapp_banner_3.jpg"), e.bannerFourUrl = P(t.banner_four_url, i + "qyapp_banner_4.png"), e.buttonOneText = t.button_one_text || "立即下载", e.buttonOneColor = t.button_one_color || "#ff8543", e.download = function () {
            function e(e) {
                c = new CallApp({
                    scheme: {protocol: "webank-cfa", host: "main"},
                    intent: {package: "com.webank.cfa", scheme: "webank-cfa"},
                    appstore: r,
                    yingyongbao: s,
                    fallback: o ? e : r,
                    timeout: 3e3
                })
            }

            function t() {
                c ? c.open({path: ""}) : window.location.href = r
            }

            q("buttonClick", "ad_enterprise_app_download", {phone_num: a});
            var n = navigator.userAgent.match(/micromessenger\/([\d.]+)/i);
            if (n) {
                var i = "https://webankcdn.net/tcftp/cfa/cpmm.html";
                window.location.href = i
            }
            var o = navigator.userAgent.match(/Android/i),
                r = (navigator.userAgent.match(/iPhone|iPad|iPod/i), "https://apps.apple.com/cn/app/%E5%BE%AE%E4%BC%97%E4%BC%81%E4%B8%9A%E7%88%B1%E6%99%AE/id1455485724"),
                s = "https://sj.qq.com/myapp/detail.htm?apkName=com.webank.cfa", c = null;
            o ? (e("https://webankcdn.net/tcftp/cfa/cpmm.html"), t()) : t()
        }, N(0)
    }

    function B(e) {
        return e || 0 === e
    }

    function H() {
        "undefined" != typeof wa && (wa.setParam("field_y_3", e.channelId), wa.setParam("field_y_4", e.advertId), wa.setParam("field_y_5", e.efs_channel_id))
    }

    function q(t, n, a) {
        if ("undefined" != typeof wa && n && B(t)) {
            var i = g.get(p.cache.session.ad_open_id.key), o = g.get(p.cache.session.ad_union_id.key);
            wa.setParam("field_y_0", o), wa.setParam("openId", i), wa.setParam("field_y_3", e.channelId), wa.setParam("field_y_4", e.advertId), wa.setParam("field_y_5", e.efs_channel_id), wa.clickStat(n, t, angular.extend({
                channel_id: e.channelId,
                advert_id: e.advertId,
                efs_channel_id: e.efs_channel_id,
                open_id: i,
                union_id: o
            }, a))
        }
    }

    function W(t) {
        e.nameCode = "6", document.title = t.page_name || "供货贷", N(0), e.btn = {
            otp: new s.ButtonOTP({
                click: function () {
                    return !this.disabled() && void S()
                }
            }), submitOrd: function () {
                I()
            }, submitBtnDisabled: !0, checkDisabled: function () {
                var t = e.input;
                e.btn.submitBtnDisabled = !(t.enterprisName.val && t.contactName.val && r.isValidPhoneNumber(t.receiverMobile.val) && t.coreEnterpriseName.val && r.isValidPhoneOtpNumber(t.otp.val))
            }
        }, e.input = {
            isCoreEnterpriseName: !1,
            coreEnterpriseSelectList: [],
            enterprisName: new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入企业名称",
                containerClass: "outerLayer",
                inputClass: "info-enter",
                labelClass: "title",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return void 0 !== e && "" !== e.trim()
                    }, err: "企业名称不能为空"
                },
                change: function () {
                    var t = e.input.enterprisName.val.length;
                    if (G && h.cancel(G), e.input.enterprisName.val || (e.isCompanyShow = !1), t > 0) {
                        var n = [], o = {data: {root: e.input.enterprisName.val}};
                        G = h(function () {
                            a.http(angular.extend(o, i.advertisement.getAssociatedNames)).then(function (t) {
                                n = t.data.list, n.length > 0 && (e.selectList = n, e.isCompanyShow = !0)
                            })
                        }, 400), e.onItemDown = function (e) {
                            event.preventDefault()
                        }, e.onItemClick = function (t) {
                            e.isCompanyShow = !1, e.input.enterprisName.val = n[t]
                        }
                    } else e.isCompanyShow = !1;
                    e.btn.checkDisabled()
                },
                blur: function () {
                    e.isCompanyShow = !1, L()
                }
            }),
            contactName: new s.Input({
                lab: "",
                val: "",
                placeholder: "请输入法人代表人姓名",
                containerClass: "outerLayer",
                inputClass: "box__input",
                labelClass: "title title--diff",
                itemClass: "consult-info__item",
                maxlength: 50,
                validation: {
                    validator: function (e) {
                        return void 0 != e && "" != e && "" != e.trim()
                    }, err: "联系人不能为空"
                },
                blur: function () {
                    L()
                },
                change: function () {
                    e.btn.checkDisabled()
                }
            }),
            receiverMobile: new s.InputMobile({
                lab: " ",
                val: "",
                placeholder: "请输入法人代表人手机号",
                containerClass: "outerLayer",
                inputClass: "box__input",
                labelClass: "title",
                itemClass: "consult-info__item",
                blur: function () {
                    L()
                },
                change: function () {
                    e.btn.checkDisabled()
                }
            }),
            coreEnterpriseName: {
                val: "", blur: function () {
                    e.input.isCoreEnterpriseName = !1, L()
                }, change: function () {
                    var t = e.input.coreEnterpriseName.val.length;
                    if (G && h.cancel(G), e.input.coreEnterpriseName.val || (e.input.isCoreEnterpriseName = !1), t > 0) {
                        var n = [], o = {data: {root: e.input.coreEnterpriseName.val}};
                        G = h(function () {
                            a.http(angular.extend(o, i.advertisement.getAssociatedNames)).then(function (t) {
                                n = t.data.list, n.length > 0 && (e.input.coreEnterpriseSelectList = n, e.input.isCoreEnterpriseName = !0)
                            })
                        }, 400), e.input.onItemDown = function (e) {
                            event.preventDefault()
                        }, e.input.onItemClick = function (t) {
                            e.input.isCoreEnterpriseName = !1, e.input.coreEnterpriseName.val = n[t]
                        }
                    } else e.input.isCoreEnterpriseName = !1;
                    e.btn.checkDisabled()
                }
            },
            otp: new s.InputOTP({
                placeholder: "请输入验证码",
                inputClass: "info-input",
                labelClass: "otp-title",
                itemClass: "consult-info__item",
                change: function () {
                    this.validate(), e.btn.checkDisabled()
                },
                blur: function () {
                    L()
                }
            })
        }, e.resetValues = function () {
            e.input.enterprisName.val = "", e.input.contactName.val = "", e.input.receiverMobile.val = "", e.input.coreEnterpriseName.val = "", e.input.otp.val = "", e.btn.otp.reset(), e.btn.checkDisabled()
        }
    }

    var Y = navigator.userAgent.match(/Android/i), V = "AD_PAGE_DATA",
        F = {HAS_ENTERPRISE_APP_DOWNLOAD: "HAS_ENTERPRISE_APP_DOWNLOAD"};
    e.area = n.area || "440001", e.advertId = n.advert_id || "1111", e.channelId = n.channel_id || r.getParamFromURL("channel_id") || "22222", e.efs_channel_id = n.efs_channel_id || r.getParamFromURL("efs_channel_id"), e.portal = n.portal || "", e.previewUrl = n.previewUrl || "", e.clickId = n.gdt_vid || r.getParamFromURL("gdt_vid"), e.requestId = n.requestId, e.url = t.absUrl(), e.ghdShow = !1, e.entShow = !1, e.ordShow = !1, e.oldShow = !1, e.appLoadShow = !1, e.qyAppLoadShow = !1, e.show = !1, e.showOrd = !1, e.showEnt = !1, e.appLoadDialogShow = !1, e.submitSuccess = !1, e.isSubmitSuccess = !1, e.existsNum = "10000", e.imgShow = !1, e.makeSure = "", e.showRules = !0, e.modelTwoTitle = "", e.buttonOneText = "", e.buttonOneColor = "", e.bannerOneUrl = "", e.bannerTwoUrl = "", e.bannerThreeUrl = "", e.bannerFourUrl = "", e.textOne = "", e.textTwo = "", e.textThree = "", e.isTextOneShow = !1, e.isTextTwoShow = !1, e.isTextThreeShow = !1, e.iconTextOne = "", e.iconTextTwo = "", e.bannerOneUrlOrd = "", e.iconOneUrl = "", e.iconTwoUrl = "", e.seriveTelOrd = "", e.isCompanyShow = !1, e.nameCode = "", e.commonDialogShow = !1, e.consumerHotline = "4009998866", e.isAdvertShow = "00154" !== e.advertId;
    var G = void 0;
    e.iconLeftImg = r.getCDNUrl() ? r.getCDNUrl() + "asset/images/ad/ent_title_left.png" : "./asset/images/ad/ent_title_left.png", e.iconRightImg = r.getCDNUrl() ? r.getCDNUrl() + "asset/images/ad/ent_title_right.png" : "./asset/images/ad/ent_title_right.png", e.iconArrowImg = r.getCDNUrl() ? r.getCDNUrl() + "asset/images/ad/depres.png" : "./asset/images/ad/depres.png", "02" === e.channelId ? e.clickId = n.qz_gdt || r.getParamFromURL("qz_gdt") : "03" === e.channelId && (e.clickId = n.clickid || r.getParamFromURL("clickid"));
    var z = {1: "success", 2: "error", 3: "warning"};
    if (e.mod = {
        show: function (t, n, a) {
            e.submitSuccess = t, e.iconType = "content-icon--" + z[n], e.submitTips = a, e.show = !0
        }, close: function () {
            e.show = !1
        }
    }, e.modEnt = {
        showEnt: function (t, n, a) {
            e.submitSuccess = t, e.isSubmitSuccess = !t, e.iconType = "content-icon--" + z[n], e.submitTips = a, e.showEnt = !0
        }, closeEnt: function () {
            e.showEnt = !1
        }
    }, e.modOrd = {
        showOrd: function (t, n, a) {
            e.submitSuccess = t, e.isSubmitSuccess = !t, e.iconType = "content-icon--" + z[n], e.submitTips = a, e.showOrd = !0
        }, closeOrd: function () {
            e.imgShow = !1, e.showOrd = !1
        }
    }, e.appLoad = {
        showOrd: function (t, n, a) {
            e.submitSuccess = t, e.isSubmitSuccess = !t, e.iconType = "content-icon--" + z[n], e.submitTips = C.trustAsHtml(a), e.appLoadDialogShow = !0
        }, closeOrd: function () {
            e.appLoadDialogShow = !1
        }
    }, e.commonDialog = {
        showCommonDialog: function (t, n, a) {
            e.submitSuccess = t, e.isSubmitSuccess = !t, e.iconType = "content-icon--" + z[n], e.submitTips = C.trustAsHtml(a), e.commonDialogShow = !0
        }, closeCommonDialog: function () {
            e.commonDialogShow = !1
        }
    }, H(), E(e.advertId)) {
        var j = g.get(V);
        j = j ? JSON.parse(j) : j;
        var J = {data: {advert_id: e.advertId, previewUrl: e.previewUrl}};
        j ? T(j, J) : a.http(angular.extend(J, i.advertisement.qryAdvertPageSet)).then(function (e) {
            var t = e.data;
            T(t, J)
        })
    }
}]), app.controller("allowListQueryController", ["$scope", "$location", "CONSTANT", "apiConfig", "$routeParams", "PageList", "feCache", "feUI", "feConfirm", "feWebService", "exchangeDetailService", "feUtils", "feToast", "commonConfig", "commonService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f) {
    function g() {
        document.body.scrollTop = 0
    }

    function _() {
        return Object.values(e.inputs).every(function (e) {
            return e.validate(), e.isValid
        })
    }

    function v() {
        function t() {
            if (_()) {
                var t = e.phoneNo, n = i.channel_id, o = e.inputs.enterprisName.val, r = e.inputs.code.val;
                l.http(angular.extend({
                    data: {
                        phone_no: t,
                        channel_id: n,
                        enterprise_name: o,
                        social_unity_credit_code: r
                    }
                }, a.allowListQuery.queryBusinessInfo)).then(function (e) {
                    e.data && e.data.exist ? b.show(e.data.enterprise_name + "，查询通过", "确定") : b.show((o || r) + "，查询不通过", "确定")
                }, function (e) {
                    23414020 == e.status && h(), b.show(e.msg, "我知道了")
                })
            }
        }

        function n() {
            e.btns.qrySubmit.disabled = !Object.keys(e.inputs).some(function (t) {
                var n = e.inputs[t].val.trim();
                return n
            })
        }

        e.showQry = !0, e.showOtp = !1, e.inputs = {
            enterprisName: new s.Input({
                lab: "企业名称",
                val: "",
                placeholder: "请输入企业名称",
                containerClass: "inquiry-group",
                inputClass: "group-input",
                warningClass: "opt-warning allow-page-warning",
                labelClass: "group-label",
                itemClass: "consult-info__item inquiry-item",
                maxlength: 20,
                change: function () {
                    n()
                }
            }),
            code: new s.Input({
                lab: "统一社会信用代码",
                val: "",
                placeholder: "请输入统一社会信用代码",
                containerClass: "inquiry-group",
                inputClass: "group-input",
                warningClass: "opt-warning allow-page-warning",
                labelClass: "group-label",
                itemClass: "consult-info__item inquiry-item",
                maxlength: 18,
                validation: {
                    validator: function (e) {
                        var t = /^[a-zA-Z\d]+$/;
                        return !("" != e && "" != e.trim() && !t.test(e))
                    }, err: "请输入正确的统一社会信用代码"
                },
                change: function () {
                    n()
                }
            })
        }, e.btns = {
            qrySubmit: new s.Button({
                text: "提交", disabled: !0, btnClass: "btn-normal", click: function () {
                    t()
                }
            })
        }
    }

    function h() {
        function t() {
            var t = e.inputs.phone.val;
            return d.isValidPhoneNumber(t) ? (e.btn.otp.countdown(), void l.http(angular.extend({data: {phone_no: t}}, a.allowListQuery.getOtp)).then(function (e) {
            })) : void p.show(m.msg.mobileNumError)
        }

        function n() {
            if (_()) {
                var t = e.inputs.phone.val, n = e.inputs.otp.val, o = i.channel_id;
                l.http(angular.extend({
                    data: {
                        phone_no: t,
                        otp_code: n,
                        channel_id: o
                    }
                }, a.allowListQuery.login)).then(function (n) {
                    e.phoneNo = t, v()
                }, function (t) {
                    23414020 == t.status ? e.inputs.otp.hide = !1 : b.show(t.msg, "我知道了")
                })
            }
        }

        e.showQry = !1, e.showOtp = !0, e.inputs = {
            phone: new s.InputMobile({
                lab: "手机号码",
                val: "",
                placeholder: "请输入您的手机号",
                containerClass: "inquiry-group",
                inputClass: "group-input",
                warningClass: "opt-warning allow-page-warning",
                labelClass: "group-label",
                itemClass: "consult-info__item",
                blur: function () {
                    g()
                }
            }),
            otp: new s.InputOTP({
                placeholder: "请输入验证码",
                hide: !0,
                containerClass: "inquiry-group",
                inputClass: "group-input",
                warningClass: "opt-warning allow-page-warning",
                labelClass: "group-label",
                itemClass: "consult-info__item",
                change: function () {
                    this.validate()
                },
                blur: function () {
                    g()
                }
            })
        }, e.btn = {
            otp: new s.ButtonOTP({
                btnClass: "group-div", click: function () {
                    return !this.disabled() && void t()
                }
            }), otpSubmit: new s.Button({
                text: "查询", btnClass: "btn-normal", click: function () {
                    n()
                }
            })
        }
    }

    /*function C() {
        h()
    }

    e.showQry = !1, e.showOtp = !1, e.phoneNo = "", i.channel_id || f.goErrorMsg({}, !0), e.diolog = {
        show: !1,
        btnText: "我知道了",
        content: "查询功能失效，不可查询!"
    };*/
    var b = {
        show: function (t, n) {
            e.diolog = {show: !0, btnText: n, content: t}
        }, close: function () {
            e.diolog = {show: !1, btnText: "", content: ""}
        }
    };
    e.closeDiolog = b.close, C()
}]), app.controller("msgController", ["$scope", "$sce", "$routeParams", "feUI", "feCache", "feCookie", "CONSTANT", "commonConfig", "feToast", "weixinService", function (e, t, n, a, i, o, r, s, c, l) {
    var u = {400: "安全认证失败", 501: "非法的请求", "50x": "系统异常，请联系客服"},
        d = i.uc.get(r.CACHE.COMMON_MSG) || {title: "出错了", content: s.msg.sysBusyError},
        p = n.errorType ? u[n.errorType] : "";
    p && (d.content = p), e.showCloseBtn = !1, e.title = d.title, e.content = t.trustAsHtml(d.content), d.showCloseBtn && (e.showCloseBtn = !0, e.closeBtn = new a.Button({
        text: "确定",
        click: function () {
            l.invoke("closeWindow")
        }
    }))
}]), app.controller("couponActivityController", ["$scope", "$location", "$routeParams", "CONSTANT", "feWebService", "feUI", "apiConfig", "commonService", "feCache", "couponService", "$sce", function (e, t, n, a, i, o, r, s, c, l, u) {
    var d = n.activity_id;
    if (!d) return void s.goErrorMsg({}, !0);
    if (e.activity = c.uc.get(a.CACHE.COUPON_ACTIVITY), e.activity) e.activity.activity_desc = u.trustAsHtml(e.activity.activity_desc.replace(/\n/g, "<br>")); else {
        var p = {data: {activity_id: d}};
        i.http(angular.extend(p, r.coupon.getActivityInfo)).then(function (t) {
            t && t.data ? (e.activity = t.data, e.activity.activity_desc = u.trustAsHtml(e.activity.activity_desc.replace(/\n/g, "<br>"))) : s.goErrorMsg({}, !0)
        })
    }
    e.btn = {
        joinBtn: new o.Button({
            text: "立即参加", waValue: "join_Btn", click: function () {
                t.path("/coupon/myCoupon").search({activity_id: d})
            }
        }), recommendBtn: new o.Button({
            text: "推荐给好友", waValue: "recommend_Btn", click: function () {
                l.showCouponActivityShareLayer({activityId: d})
            }
        })
    }
}]), app.controller("couponActivityDetailsController", ["$scope", "$sce", "feCache", "CONSTANT", "$routeParams", "commonService", "apiConfig", "feWebService", function (e, t, n, a, i, o, r, s) {
    var c = n.uc.get(a.CACHE.COUPON_ACTIVITY), l = i.activity_id;
    if (c) e.data = {
        activityDecr: c.activity_desc,
        activityRules: t.trustAsHtml(c.activity_rule_desc.replace(/\n/g, "<br>"))
    }; else {
        if (!l) return void o.goErrorMsg({}, !0);
        var u = {data: {activity_id: l}};
        s.http(angular.extend(u, r.coupon.getActivityInfo)).then(function (i) {
            i && i.data ? (n.uc.put(a.CACHE.COUPON_ACTIVITY, i.data), e.data = {
                activityDecr: i.data.activity_desc,
                activityRules: t.trustAsHtml(i.data.activity_rule_desc.replace(/\n/g, "<br>"))
            }) : o.goErrorMsg({}, !0)
        }, function () {
            o.goErrorMsg({msg: "暂时无法获取活动信息"}, !0)
        })
    }
}]), app.controller("followOfficialAccountsController", ["$scope", "feUtils", "feEnvService", "publicConfig", function (e, t, n, a) {
    var i = "";
    i = t.getCDNUrl() && "undefined/" !== t.getCDNUrl() ? t.getCDNUrl() : "./", e.qrcodeImg = i + a.qrcodeURL, e.WeChatSubsription = a.WeChatSubsription
}]);
var _slicedToArray = function () {
    function e(e, t) {
        var n = [], a = !0, i = !1, o = void 0;
        try {
            for (var r, s = e[Symbol.iterator](); !(a = (r = s.next()).done) && (n.push(r.value), !t || n.length !== t); a = !0) ;
        } catch (e) {
            i = !0, o = e
        } finally {
            try {
                !a && s.return && s.return()
            } finally {
                if (i) throw o
            }
        }
        return n
    }

    return function (t, n) {
        if (Array.isArray(t)) return t;
        if (Symbol.iterator in Object(t)) return e(t, n);
        throw new TypeError("Invalid attempt to destructure non-iterable instance")
    }
}();
app.controller("myCouponController", ["$scope", "$location", "$routeParams", "CONSTANT", "commonConfig", "feWebService", "feUI", "apiConfig", "commonService", "couponService", "feCache", "feToast", "feAlert", function (e, t, n, a, i, o, r, s, c, l, u, d, p) {
    var m = n.activity_id;
    if (!m) return void c.goErrorMsg({}, !0);
    var f = i.coupon.slogen;
    e.stepstatus = {status: ["领取成功", "绑定成功", "使用成功"], current: 0}, e.actions = {
        btnText: "",
        btnWaValue: "",
        disabled: !1,
        status: 0,
        slogen: ""
    };
    var g = {data: {activity_id: m}}, _ = function () {
        o.http(angular.extend(g, s.coupon.getActivityInfo)).then(function (t) {
            t && t.data ? (e.couponId = t.data.free_coupons_id, u.uc.put(a.CACHE.COUPON_ACTIVITY, t.data), e.activity = {
                activityName: t.data.activity_name,
                start: "Y" == t.data.is_start,
                end: !("N" == t.data.is_end || !t.data.is_end),
                current: 0,
                couponName: t.data.free_coupons_name,
                faceValue: t.data.face_value,
                maxLoanAmt: t.data.max_loan_amt,
                effectivedays: t.data.effective_days,
                activityDate: t.data.start_time.slice(0, 10) + "~" + t.data.end_time.slice(0, 10),
                activity_desc: t.data.activity_desc,
                is_receive: "Y" == t.data.is_receive,
                is_quota: "Y" == t.data.is_quota,
                is_used: "Y" == t.data.is_used
            }, v({
                is_receive: e.activity.is_receive,
                is_quota: e.activity.is_quota,
                is_used: e.activity.is_used
            }), h(e.activity.end, e.activity.is_receive, e.activity.is_quota, e.activity.is_used), e.couponBtn = new r.Button({
                text: e.actions.btnText,
                waValue: e.actions.btnWaValue,
                disabled: e.actions.disabled,
                click: function () {
                    var t = this, n = function () {
                        return new Map([[{status: 0}, function () {
                            C()
                        }], [{status: 2}, function () {
                            l.goConponList({free_coupons_type: "01"})
                        }], [{status: 3}, function () {
                            l.goConponList({free_coupons_type: "02"})
                        }], [{status: 4}, function () {
                            l.goConponList({free_coupons_type: "02"})
                        }]])
                    }, a = [].concat(_toConsumableArray(n())).filter(function (t) {
                        var n = _slicedToArray(t, 2), a = n[0];
                        n[1];
                        return a.status == e.actions.status
                    });
                    a.forEach(function (e) {
                        var n = _slicedToArray(e, 2), a = (n[0], n[1]);
                        return a.call(t)
                    })
                }
            })) : c.goErrorMsg({}, !0)
        }, function () {
            c.goErrorMsg({msg: "暂时无法获取活动信息"}, !0)
        })
    };
    _(), e.evt = {
        checkDetails: function () {
            t.path("/coupon/couponActivityDetails")
        }, following: function () {
            t.path("/coupon/followOfficialAccounts")
        }
    };
    var v = function () {
        var t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {}, n = t.is_receive || !1,
            a = t.is_quota || !1, i = t.is_used || !1;
        n && (e.stepstatus.current = 1), n && a && (e.stepstatus.current = 2), i && (e.stepstatus.current = 3)
    }, h = function (t, n, a, i) {
        var o = function (t, n, a, i) {
            e.actions = {btnText: t, btnWaValue: n, disabled: a, status: i || 0, slogen: f[i].text}
        };
        return t || n ? t && !n ? void o("已结束", "is_end_btn", !0, 1) : n && !a ? void o("去绑定", "apply_btn", !1, 2) : i ? void (i && o("已使用", "used_btn", !0, 5)) : t ? void o("去使用", "apply_btn", !1, 4) : void o("去使用", "apply_btn", !1, 3) : void o("领取", "coupon_receive_btn", !1, 0)
    }, C = function () {
        return e.activity.start ? void o.http(angular.extend(g, s.coupon.seize)).then(function (t) {
            if (t && t.data) {
                var n = t.data.result;
                switch (n) {
                    case"0":
                        var i = u.uc.get(a.CACHE.COUPON_ACTIVITY);
                        u.uc.put(a.CACHE.COUPON_ACTIVITY, angular.extend(i, t.data)), b({
                            title: "恭喜老板",
                            content: "领取成功",
                            result: n
                        });
                        break;
                    case"1":
                        b({title: "来晚了", content: "本期免息券已被抢光，敬请期待下期活动！", result: n}), e.actions.slogen = f[6].text;
                        break;
                    case"2":
                        e.activity.end = !0;
                        break;
                    case"3":
                        d.showMask("您已参与过本活动", 3e3);
                        break;
                    case"99":
                        d.showMask("当前参与活动的人太多，请退出后重新进入领取页面", 3e3);
                        break;
                    case"4":
                        b({title: "领取失败", content: "抱歉！您暂不符合本期活动规则！", result: n});
                        break;
                    default:
                        c.goErrorMsg({}, !0)
                }
            } else c.goErrorMsg({}, !0)
        }) : void b({title: "领取失败", content: "活动未开始"})
    }, b = function (e) {
        var t = e.title || "", n = e.content || "", a = e.btnText || "朕知道了", i = e.result || 0;
        p.show({
            title: t, content: n, btn: {
                text: a, click: function () {
                    "0" == i && _(), document.querySelectorAll(".mod-mask")[0].classList.remove("coupon-mod")
                }
            }
        }), document.querySelectorAll(".mod-mask")[0].classList.add("coupon-mod")
    }
}]), app.controller("emptyCouponController", ["$scope", "$routeParams", "commonService", function (e, t, n) {
    var a = {"01": "活动太火热<br>免息券被抢完"}, i = t.type;
    i && a.hasOwnProperty(i) ? e.msg = a[i] : n.goErrorMsg({}, !0)
}]), app.controller("getShareCouponController", ["$scope", "$location", "$routeParams", "CONSTANT", "apiConfig", "feCache", "feWebService", "commonService", "couponService", "feToast", "publicConfig", function (e, t, n, a, i, o, r, s, c, l, u) {
    e.couponId = n.free_coupons_id, e.productName = u.productName;
    var d = n.transfer_id;
    if (!e.couponId || !d) return void s.goErrorMsg({}, !0);
    if (e.activity = o.uc.get(a.CACHE.COUPON_ACTIVITY), !e.activity) {
        var p = {data: {free_coupons_id: e.couponId, transfer_id: d}};
        r.http(angular.extend(p, i.coupon.queryCoupon)).then(function (t) {
            t && t.data && (e.activity = t.data)
        })
    }
    e.btn = {
        share: function () {
            var t = e.couponId;
            c.showCouponShareLayer({couponId: t})
        }, getCoupon: function () {
            var n = {data: {transfer_id: d, free_coupons_id: e.couponId}};
            r.http(angular.extend(n, i.coupon.getCoupon)).then(function (e) {
                if (e && e.data) {
                    var n = e.data.result;
                    "0" == n || "1" == n ? t.path("/coupon/share").search({
                        free_coupons_id: e.data.free_coupons_id,
                        transfer_id: d,
                        force: !0
                    }) : "2" == n || "3" == n ? t.path("/coupon/notice").search({
                        type: "01",
                        is_self: "N"
                    }) : "99" == n ? l.showMask("当前参与活动的人太多，请退出后重新进入领取页面", 3e3) : s.goErrorMsg({}, !0)
                }
            })
        }
    }
}]), app.controller("couponController", ["$scope", "$location", "$routeParams", "CONSTANT", "feWebService", "apiConfig", "commonService", "feCache", "feToast", function (e, t, n, a, i, o, r, s, c) {
    var l = n.activity_id;
    if (!l) return void r.goErrorMsg({}, !0);
    e.show = {contents: !1, end: !1};
    var u = {data: {activity_id: l}};
    i.http(angular.extend(u, o.coupon.getActivityInfo)).then(function (n) {
        if (n && n.data) {
            var i = n.data.free_coupons_id;
            if ("Y" != n.data.is_start) return void t.path("/coupon/notice").search({
                type: "03",
                start_time: n.data.start_time
            });
            if (s.uc.put(a.CACHE.COUPON_ACTIVITY, n.data), e.show.content = !0, n.data.max_loan_amt && n.data.effective_days > 0) {
                if (e.activity = {
                    useRule: n.data.max_loan_amt,
                    effectiveDays: n.data.effective_days,
                    faceValue: n.data.face_value
                }, "Y" == n.data.is_end) e.show.end = !0; else if (i) return void t.path("/coupon/share").search({
                    free_coupons_id: i,
                    force: !0
                })
            } else r.goErrorMsg({}, !0)
        } else r.goErrorMsg({}, !0)
    }, function () {
        r.goErrorMsg({msg: "暂时无法获取活动信息"}, !0)
    }), e.btn = {
        seize: function () {
            i.http(angular.extend(u, o.coupon.seize)).then(function (n) {
                if (n && n.data) {
                    var i = n.data.result;
                    switch (i) {
                        case"0":
                            var o = s.uc.get(a.CACHE.COUPON_ACTIVITY);
                            s.uc.put(a.CACHE.COUPON_ACTIVITY, angular.extend(o, n.data)), t.path("/coupon/share").search({free_coupons_id: n.data.free_coupons_id});
                            break;
                        case"1":
                            t.path("/coupon/empty").search({type: "01"});
                            break;
                        case"2":
                            e.show.end = !0;
                            break;
                        case"3":
                            c.showMask("您已参与过本活动", 3e3);
                            break;
                        case"99":
                            c.showMask("当前参与活动的人太多，请退出后重新进入领取页面", 3e3);
                            break;
                        default:
                            r.goErrorMsg({}, !0)
                    }
                } else r.goErrorMsg({}, !0)
            })
        }
    }
}]), app.controller("couponListController", ["$scope", "$routeParams", "$location", "$q", "CONSTANT", "feUtils", "apiConfig", "feConfirm", "envConfig", "commonService", "commonConfig", "feWebService", "couponService", "productService", "PageList", "feToast", "feSelectPopup", "feSession", "browserService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h) {
    var C = t.free_coupons_type, b = t.subType || o.getParamFromURL("subType"), y = i.COUPON.FREE_COUPONS_TYPE,
        w = i.COUPON.QUERY_DIMENSION_TYPE, S = JSON.parse(v.get(u.cache.session.cpmm_route_params.key) || "{}"),
        E = null;
    e.entList = [], e.currentEnterprise = {}, e.isFromLimitPage = b === i.APP.SUB_TYPE.MANAGE;
    var T = p.isNN();
    T && h.filter({handleBack: "close"});
    var A = function () {
        var e = [{label: "未绑定", couponType: y.UNBIND, couponListClass: "unbond"}, {
            label: "待使用",
            couponType: y.UNUSED,
            couponListClass: "unused"
        }, {label: "不可用", couponType: y.UNAPPLICABLE, couponListClass: "unvailable"}];
        return e
    }, N = function () {
        for (var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : [], t = 0, n = 0, a = e.length; n < a; n++) e[n].couponType === C && (t = n);
        return t
    }, I = function (e) {
        return !T && "02" === e
    }, x = function () {
        var e = {};
        return e.tabs = A(), e.currentIndex = N(e.tabs), e.showGoLoanBtn = I(e.tabs[e.currentIndex].couponType), e
    };
    e.navData = x(), e.couponsData = {list: [], couponListEmpty: !0, couponListClass: "unbond"}, e.evt = {
        changeNav: function (t) {
            var n = e.navData;
            (!t || t > n.length - 1) && (t = 0), e.navData.currentIndex = t, e.navData.showGoLoanBtn = I(e.navData.tabs[t].couponType), L()
        }, bindCoupon: function (t) {
            e.isFromLimitPage ? p.bindCoupon(t, S.ccif).then(function () {
                e.evt.changeNav(e.navData.currentIndex + 1), g.show(u.msg.bindCouponSuccess), e.evt.selectCoupon(t)
            }) : p.getEntList(t).then(function (n) {
                var a = n.filter(function (e) {
                    return e.valid
                }).map(function (e) {
                    return {value: e.ccif_no, text: e.org_name}
                });
                a.length <= 0 && s.show({
                    content: u.msg.noCouponApplyCompany,
                    btn: {
                        primary: {
                            text: "去申请", click: function () {
                                s.hide(), l.goMesp()
                            }
                        }
                    }
                }), _.show({
                    current: -1, options: a, click: function (n) {
                        _.current = n.value, p.bindCoupon(t, n.value).then(function () {
                            P(n), _.hide(), e.evt.changeNav(e.navData.currentIndex + 1), g.show(u.msg.bindCouponSuccess), e.evt.selectCoupon(t)
                        })
                    }, title: "选择免息券绑定企业"
                })
            })
        }, selectCoupon: function (t) {
            e.selectedCouponCode == t ? e.selectedCouponCode = "" : e.selectedCouponCode = t
        }, goLoan: function () {
            l.goMesp({cpmmEntryType: "loanApply", cpmmFreeCouponId: e.selectedCouponCode})
        }, changeBindEnterprise: function () {
            _.show({
                current: e.currentEnterprise.value, options: e.entList, click: function (e) {
                    P(e), _.hide(), L()
                }, title: "更换企业"
            })
        }
    };
    var L = function () {
        E = null, k(), e.couponsData.couponList.nextPage()
    }, P = function (t) {
        e.currentEnterprise = t ? t : e.entList[0] || {}, _.current = e.currentEnterprise.value
    }, k = function () {
        var t = e.navData.tabs[e.navData.currentIndex].couponType, n = {free_coupons_type: t};
        t === y.UNBIND ? n.query_dimension_type = w.USER : t === y.UNUSED ? (n.query_dimension_type = w.ENTERPRISE, n.ccif = e.currentEnterprise.value) : (n.query_dimension_type = e.isFromLimitPage ? w.ENTERPRISE : w.USER_WITH_ENTERPRISE, n.ccif = e.currentEnterprise.value), e.isFromLimitPage && S.ccif && (n.ccif = S.ccif, n.product_id = S.product_id, n.com_product_id = S.com_product_id, n.project_id = S.project_id), e.couponsData.couponList = new f({
            pageSize: 10,
            pageNumber: 1,
            requestParam: angular.extend(r.coupon.getCouponList, {data: n}),
            transformRequest: function () {
                return n.start_index = E, n.page_size = 10, delete n.pageNumber, n
            },
            transformResponse: function (t) {
                return e.isFromLimitPage && t.enterpriseName && (e.currentEnterprise = {text: t.enterpriseName}), E = t.nextIndex, p.formatCouponListData(t.list, n.free_coupons_type)
            },
            successCallback: function () {
                this.isEnd = !E, e.couponsData.couponListEmpty = e.couponsData.couponList.items.length <= 0
            },
            errorCallback: function () {
            }
        }), e.couponsData.couponListClass = e.navData.tabs[e.navData.currentIndex].couponListClass
    };
    e.isFromLimitPage ? k() : p.getEntList().then(function (t) {
        e.entList = t.map(function (e) {
            return {value: e.ccif_no, text: e.org_name}
        }), P(), k(), e.couponsData.couponList.refresh()
    })
}]), app.controller("couponListForWpmController", ["$scope", "$routeParams", "CONSTANT", "apiConfig", "envConfig", "commonService", "feWebService", "couponService", "productService", "PageList", function (e, t, n, a, i, o, r, s, c, l) {
    var u = null;
    e.submitBtnText = "去借款", e.couponsData = {
        list: [],
        couponListEmpty: !1,
        couponListClass: "unbond"
    }, e.evt = {
        bindCoupon: function (t) {
            s.bindCoupon(t).then(function () {
                e.selectedCouponCode = t, e.evt.goLoan()
            })
        }, shareCoupon: function (e) {
            s.showCouponShareLayer({couponId: e})
        }, selectCoupon: function (t) {
            e.selectedCouponCode == t ? e.selectedCouponCode = "" : e.selectedCouponCode = t
        }, goLoan: function () {
            s.goWpmLoanApply({cpmmFreeCouponId: e.selectedCouponCode})
        }
    };
    var d = {
        free_coupons_type: n.COUPON.FREE_COUPONS_TYPE.VALID,
        query_dimension_type: n.COUPON.QUERY_DIMENSION_TYPE.USER_WITH_ENTERPRISE
    };
    e.couponsData.couponList = new l({
        pageSize: 10,
        pageNumber: 1,
        requestParam: angular.extend(a.coupon.getCouponList, {data: d}),
        transformRequest: function () {
            return d.start_index = u, d.page_size = 10, delete d.pageNumber, d
        },
        transformResponse: function (e) {
            return u = e.nextIndex, s.formatCouponListData(e.list)
        },
        successCallback: function () {
            this.isEnd = !u, e.couponsData.couponListEmpty = e.couponsData.couponList.items.length <= 0
        },
        errorCallback: function () {
        }
    })
}]), app.controller("noticeController", ["$scope", "$routeParams", "CONSTANT", "apiConfig", "commonService", "couponService", "feToast", "feWebService", "feUtils", "fePageService", "feCache", "publicConfig", function (e, t, n, a, i, o, r, s, c, l, u, d) {
    var p = t.is_self || "Y";
    e.WeChatSubsription = d.WeChatSubsription;
    var m = function (t) {
        var n = {data: {free_coupons_id: t}};
        s.http(angular.extend(n, a.coupon.share)).then(function (t) {
            if (t && t.data) {
                var n = t.data.result;
                if ("0" == n) {
                    var a = o.getShareUrl(e.free_coupons_id, t.data.transfer_id);
                    e.url = a
                } else "1" == n || "2" == n ? r.show("您的免息券已被领取") : i.goErrorMsg({msg: "分享失败"}, !0)
            }
        })
    }, f = {
        "01": {
            title: "该券已过期或已被绑定",
            content: "抱歉，该" + (d.productName2 || "") + "免息券已过期或已被绑定。您可以关注" + d.WeChatSubsription + "，参与相关活动获取免息券。",
            show: !1,
            isShare: !1
        },
        "02": {title: "您已经分享过该免息券", content: "您已经分享过该免息券，若需分享给其他好友，请出示以下二维码，或点击立即分享进行重新分享。", show: !0, isShare: !0},
        "03": {show: !1, isShare: !1}
    }, g = t.type;
    if (!g) return void i.goErrorMsg({}, !0);
    if (!f.hasOwnProperty(g)) return void i.goErrorMsg({}, !0);
    if (e.notice = f[g], "02" == g) {
        e.free_coupons_id = t.free_coupons_id, m(e.free_coupons_id);
        var _ = u.uc.get(n.CACHE.COUPON_ACTIVITY);
        if (_) e.faceValue = _.face_value; else {
            var v = {data: {free_coupons_id: e.free_coupons_id, transfer_id: t.transfer_id}};
            s.http(angular.extend(v, a.coupon.queryCoupon)).then(function (t) {
                var n = t.data;
                "1" == n.free_coupons_status || "0" == n.free_coupons_status ? e.faceValue = n.face_value : "2" == n.free_coupons_status ? $location.path("/coupon/share").search(params) : "3" == n.free_coupons_status ? $location.path("/coupon/notice").search({type: "01"}) : i.goErrorMsg({}, !0)
            })
        }
    } else "03" == g && (t.start_time ? e.notice.title = "该活动开始时间为<br>" + c.formatDate(t.start_time) + "<br>敬请关注!" : i.goErrorMsg({msg: "缺少活动开始日期"}, !0));
    e.btn = {
        share: function () {
            if (e.free_coupons_id) {
                var t = {couponId: e.free_coupons_id};
                e.url && (t = angular.extend(t, {url: e.url})), o.showCouponShareLayer(t)
            } else i.goErrorMsg({}, !0)
        }
    }, l.setPageTitle("Y" == p ? "免息券" : "领取结果")
}]), app.controller("couponShareController", ["$scope", "$routeParams", "$location", "CONSTANT", "apiConfig", "commonService", "feCache", "feWebService", "feQRCode", "feCookie", "commonConfig", "weixinService", "feUtils", "couponService", "fePageService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f) {
    var g = {
        SELF_NORMAL: {title: "", beReceived: !1, unable: !1, btn: {enable: !0, text: "立即申请"}, pageTitle: "免息券"},
        SELF_UNABLE: {title: "", beReceived: !0, unable: !0, btn: {enable: !0, text: "立即申请"}, pageTitle: "免息券"},
        OTHER_NORMAL: {title: "已领取", beReceived: !1, unable: !1, btn: {enable: !0, text: "立即申请"}, pageTitle: "领取结果"},
        OTHER_UNABLE: {title: "领取失败", beReceived: !1, unable: !0, btn: {enable: !1, text: "已被抢走"}, pageTitle: "领取结果"}
    };
    if (e.couponId = t.free_coupons_id, !e.couponId) return void o.goErrorMsg({}, !0);
    var _ = t.transfer_id;
    e.activity = r.uc.get(a.CACHE.COUPON_ACTIVITY);
    var v = function () {
        var a = e.activity.free_coupons_status;
        a ? "99" == a || "0" == a ? o.goErrorMsg({msg: "异常免息券类型"}, !0) : "3" == a ? n.path("/coupon/notice").search({
            type: "01",
            is_self: e.activity.is_self
        }) : !_ || e.activity.is_self && "Y" == e.activity.is_self ? "1" == a ? e.status = g.SELF_NORMAL : "2" == a ? e.status = g.SELF_UNABLE : o.goErrorMsg({msg: "异常免息券类型"}, !0) : "1" == a ? (e.status = g.OTHER_NORMAL, t.force && (e.status.title = "领取成功")) : "2" == a ? e.status = g.OTHER_UNABLE : o.goErrorMsg({msg: "异常免息券类型"}, !0) : e.status = g.SELF_NORMAL, e.status && f.setPageTitle(e.status.pageTitle)
    };
    if (!e.activity || t.force) {
        var h = {data: {free_coupons_id: e.couponId, transfer_id: _}};
        s.http(angular.extend(h, i.coupon.queryCoupon)).then(function (t) {
            t && t.data && (e.activity = t.data, v())
        })
    } else v();
    e.btn = {
        share: function () {
            var t = e.couponId;
            m.showCouponShareLayer({couponId: t})
        }, goMesp: function () {
            e.status.btn.enable && o.goMesp()
        }
    }
}]), app.controller("exchangeDetailController", ["$scope", "$location", "apiConfig", "CONSTANT", "feCache", "feWebService", "feUI", "feConfirm", "feAnalyticsService", "exchangeDetailService", function (e, t, n, a, i, o, r, s, c, l) {
    var u = e.prize = i.uc.get(a.CACHE.GIFT_EXCHANGE);
    u || t.path("/mgm/giftExchange"), e.showAddBtn = !1, e.showDefautAddress = !1, e.defaultAddress = {
        city_name: "",
        address_id: "",
        province_name: "",
        district_name: "",
        street_detail: ""
    }, e.button = new r.Button({
        text: "确定兑换", disabled: !0, click: function () {
            s.show({
                title: "确定使用" + u.worth_coins + "奖励金<br/>兑换" + u.award_name + "奖品?",
                content: "确定兑换后将扣除相应兑换的奖励金，且地址不可修改，订单不可取消，确认您的订单信息无误",
                contentLeft: !0,
                btn: {
                    primary: {
                        text: "确认兑换", click: function () {
                            c.clickStat(c.EVENT_NAME.BUTTON_CLICK, "mgm_exchangeDetail_confirmBtn"),
                                s.hide();
                            var a = e.defaultAddress,
                                i = [a.province_name, a.city_name, a.district_name, a.street_detail].join(" "), r = {
                                    data: {
                                        award_id: u.award_id,
                                        address_id: a.address_id,
                                        address_detail: i,
                                        activity_id: u.activity_id
                                    }
                                };
                            o.http(angular.extend(r, n.gift.exchangedAward)).then(function (e) {
                                var n = e.data;
                                if (n) return 0 == n.exchangedResult ? void t.path("/mgm/exchangeStatus").search({exchangedResult: n.exchangedResult}) : 1 == n.exchangedResult ? (l.setAwardName(u.award_name), void t.path("/mgm/exchangeStatus").search({exchangedResult: n.exchangedResult})) : void 0
                            })
                        }
                    }, hide: {text: "暂不兑换"}
                }
            })
        }
    }), e.goEditAddress = function () {
        t.path("/address/editAddress").search({type: 1})
    }, e.goManageAddress = function () {
        c.clickStat(c.EVENT_NAME.BUTTON_CLICK, "mgm_exchangeDetail_goManageAddressBtn"), t.path("/address/manageAddress")
    };
    var d = function () {
        var t = {data: {pageNumber: 1, pageSize: 1, isDefault: "Y"}};
        o.http(angular.extend(t, n.address.getAddressList)).then(function (t) {
            t.data && t.data.list && t.data.list.length > 0 ? (e.defaultAddress = t.data.list[0], e.showDefautAddress = !0, e.button.disabled = !1) : e.showAddBtn = !0
        })
    }, p = function () {
        var t = l.getChooseAddress();
        t && null != t ? (e.defaultAddress = t, e.showDefautAddress = !0, e.button.disabled = !1) : d()
    };
    0 == u.award_type ? (e.isVirtualPrize = !0, e.button.disabled = !1) : p()
}]), app.factory("exchangeDetailService", function () {
    var e = "", t = null, n = function (t) {
        e = t
    }, a = function () {
        return e
    }, i = function (e) {
        t = e
    }, o = function () {
        return t
    };
    return {setAwardName: n, getAwardName: a, setChooseAddress: i, getChooseAddress: o}
}), app.controller("exchangeStatusController", ["$scope", "$routeParams", "$location", "weixinService", "feUI", "exchangeDetailService", "CONSTANT", function (e, t, n, a, i, o, r) {
    var s = t.exchangedResult;
    if (e.exchangeStatus = {outAwardName: ""}, 1 == s) {
        var c = o.getAwardName();
        e.exchangeStatus.outAwardName = c ? c : "该"
    }
    var l = [{
        icon: "ui-icon-warn",
        title: e.exchangeStatus.outAwardName + "奖品已被兑换完<br>请兑换其他奖品"
    }, {
        icon: "ui-icon-success",
        title: "奖品兑换成功",
        msg: '请等待发放，我们将在一个月内发放完成<br>如有疑问，可拨打：<a href="tel:' + r.APP.HOTLINE + '">' + r.APP.HOTLINE + "</a>"
    }];
    e.ui = 1 == s ? l[0] : l[1], e.button = new i.Button({
        text: 1 == s ? "返回兑换奖品" : "确认", click: function () {
            1 == s ? n.path("/mgm/giftExchange") : n.path("/mgm/giftExchange")
        }
    })
}]), app.controller("exchangedListController", ["$scope", "$location", "$routeParams", "commonConfig", "feCookie", "apiConfig", "feUI", "CONSTANT", "PageList", function (e, t, n, a, i, o, r, s, c) {
    var l, u = (l = {}, _defineProperty(l, s.APP.EXCHANGEDSTATUS.SHIPPED, {
        val: "已发出",
        color: "gray"
    }), _defineProperty(l, s.APP.EXCHANGEDSTATUS.NOTSHIPPED, {val: "待发出", color: "orange"}), l);
    e.goExchangeBtn = new r.Button({
        text: "去兑换", click: function () {
            t.path("/mgm/giftExchange")
        }
    });
    var d = a.cache.cookie, p = i.get(d.mgm_activity_id.key);
    angular.extend(o.gift.getExchangedAwards, {data: {activity_id: p}}), e.exchangeList = new c({
        requestParam: o.gift.getExchangedAwards,
        transformResponse: function (t) {
            t && t.list && t.list.length ? e.notEmpty = !0 : e.empty = new r.EmptyList({
                isEmpty: !0,
                title: "当前没有兑换记录",
                iconClass: " "
            });
            var n = !0, a = !1, i = void 0;
            try {
                for (var o, s = (t.list || [])[Symbol.iterator](); !(n = (o = s.next()).done); n = !0) {
                    var c = o.value;
                    c.statusVal = u[c.status].val, c.statusColor = u[c.status].color, "0" == c.award_type && (c.statusVal = "待使用")
                }
            } catch (e) {
                a = !0, i = e
            } finally {
                try {
                    !n && s.return && s.return()
                } finally {
                    if (a) throw i
                }
            }
            return t.list
        }
    })
}]), app.controller("giftExchangeController", ["$scope", "$location", "CONSTANT", "commonConfig", "feUtils", "feCookie", "apiConfig", "feUI", "feCache", "feWebService", "feToast", "PageList", "marketingService", "feAnalyticsService", "exchangeDetailService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f) {
    var g = void 0;
    e.notZero = !1, e.canExchange = !1, e.awardValid = !0, e.goExchangeHistory = function () {
        m.clickStat(m.EVENT_NAME.BUTTON_CLICK, "mgm_giftExchange_goExchangeListBtn"), t.path("/mgm/exchangedList")
    }, e.goExchangeDetail = function () {
        g ? (m.clickStat(m.EVENT_NAME.BUTTON_CLICK, "mgm_giftExchange_goExchangeDetailBtn"), c.uc.extend(n.CACHE.GIFT_EXCHANGE, g), f.setChooseAddress(null), t.path("/mgm/exchangeDetail")) : u.show(a.msg.plsChooseGift)
    }, e.check = function (t) {
        e.giftList.items[t].giftStatus[1] && (m.clickStat(m.EVENT_NAME.BUTTON_CLICK, "mgm_giftExchange_checkBtn"), g && (g.checked = !1), e.giftList.items[t].checked = !0, g = e.giftList.items[t])
    };
    var _ = function () {
        var t = a.cache.cookie, n = o.get(t.mgm_activity_id.key);
        l.http(angular.extend({data: {activity_id: n}}, r.activity.getActivityInfo)).then(function (t) {
            if (e.mgmUserInfo = t.data, e.mgmUserInfo.award_coins_expire_time = i.formatDate(e.mgmUserInfo.award_coins_expire_time), 0 == t.data.current_award_coins) {
                var a = "Y" == t.data.is_end;
                e.empty = new s.EmptyList({
                    isEmpty: !0,
                    title: "没有奖励金可兑换奖品",
                    iconClass: "",
                    desc: "快去邀请小伙伴们加入<br>即可获得奖励金兑换奖品",
                    button: new s.Button({
                        text: "立即邀请", show: !0, disabled: !!a, click: function () {
                            a || p.showMgmShareMenu()
                        }
                    })
                })
            } else e.notZero = !0, angular.extend(e.giftList.requestParam, {data: {activity_id: n}}), e.giftList.refresh(), "Y" == t.data.is_expired && (e.canExchange = !1, e.awardValid = !1)
        })
    }, v = function () {
        e.giftList = new d({
            requestParam: r.gift.getAvailableAwards, autoLoad: !1, transformResponse: function (t) {
                if (!t || !t.list) return [];
                e.notZero && (t.list.length ? (e.notEmpty = !0, e.awardValid && (e.canExchange = !0)) : e.empty = new s.EmptyList({
                    isEmpty: !0,
                    title: "没有可兑换奖品",
                    iconClass: "ui-icon-banner-empty"
                }));
                var n = e.mgmUserInfo.current_award_coins, a = !0, i = !1, o = void 0;
                try {
                    for (var r, c = (t.list || [])[Symbol.iterator](); !(a = (r = c.next()).done); a = !0) {
                        var l = r.value, u = l.worth_coins - n;
                        l.giftStatus = [!1, !1, !1], l.gap = u, l.checked = !1, 0 == l.award_status ? l.giftStatus[2] = !0 : u > 0 ? l.giftStatus[0] = !0 : l.giftStatus[1] = !0
                    }
                } catch (e) {
                    i = !0, o = e
                } finally {
                    try {
                        !a && c.return && c.return()
                    } finally {
                        if (i) throw o
                    }
                }
                return t.list
            }
        })
    }, h = function () {
        _(), v()
    };
    h()
}]), app.controller("newLeaveController", ["$scope", "$location", "$routeParams", "feWebService", "apiConfig", "commonService", "feUtils", "feUI", "feLoading", "feEnvService", "envConfig", "$http", "commonConfig", "feCookie", "feCache", "feSession", "CONSTANT", "feToast", "$timeout", "$sce", "weixinService", "marketingService", "feAnalyticsService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h, C, b, y, w) {
    var S = p.cache.session.page_data_session_key.key, E = g.get(S);
    E = E ? JSON.parse(E) : {}, g.remove(S)
}]), app.controller("leaveStaticforDHDController", ["$scope", "$location", "$routeParams", "feWebService", "apiConfig", "commonService", "feUtils", "feUI", "feLoading", "feEnvService", "envConfig", "$http", "commonConfig", "feCookie", "feCache", "feSession", "CONSTANT", "feToast", "$timeout", "$sce", "weixinService", "marketingService", "feAnalyticsService", "leaveMessageService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h, C, b, y, w, S) {
    function E() {
        a.http(angular.extend({}, i.activity.scrollingDisplay)).then(function (t) {
            if (t.data && t.data.list) {
                var n = t.data.list;
                if (Array.isArray(n) && n.length > 0) for (var a = 0; e.scrollList.length < 10;) {
                    n[a] || (a = 0);
                    var i = JSON.parse(JSON.stringify(n[a]));
                    i.timeStr = R[a], i.creditAmt = Math.floor(i.creditAmt / 1e4), e.scrollList.push(i), a++
                }
            }
        })
    }

    function T() {
        var t = e.input.receiverMobile.val;
        return r.isValidPhoneNumber(t) ? (e.btn.otp.countdown(), void a.http(angular.extend({
            data: {
                phone_no: t,
                advert_id: e.advertId
            }
        }, i.common.getOtp)).then(function (e) {
        })) : void v.show(p.msg.mobileNumError)
    }

    function A(t) {
        e.diologShow = !0, e.tips = t
    }

    function N() {
        if (!e.advertId || !e.channelId) return void A("留资ID或渠道号获取不到！");
        if (I()) {
            var t = {
                data: {
                    product_brand_name: e.input.brandName.val,
                    brand_name: e.input.brandName.val,
                    enterprise_name: e.input.enterprisName.val,
                    contact_name: e.input.contactName.val,
                    contact_phone_no: e.input.receiverMobile.val,
                    channel_id: e.channelId,
                    advert_id: e.advertId,
                    efs_channel_id: e.efs_channel_id,
                    url: e.url,
                    request_id: e.requestId,
                    otp_code: e.input.otp.val,
                    activity_id: U,
                    inviter_union_id: D
                }
            };
            w.bpButtonClick("dhd_assetsLeaveMessage_submit"), a.http(angular.extend(t, i.advertisement.submitConsumerInfo)).then(function (e) {
                window.location.href = r.appendParamsToRouter(u.mgm2Prefix + "result/dhd", {mgmActivityId: U})
            }, function (e) {
                A(e.msg)
            })
        }
    }

    function I() {
        var t = !0;
        for (var n in e.input) if (e.input.hasOwnProperty(n) && e.input[n].validate && (e.input[n].validate(), !e.input[n].isValid)) {
            t = !1;
            break
        }
        return t
    }

    e.consumerHotline = "0755-88882018";
    var x = "asset/images/ad/serive_icon.png";
    e.seriveTelOrd = r.getCDNUrl() ? r.getCDNUrl() + x : "./" + x;
    var L = p.cache.session.page_data_session_key.key, P = g.get(L);
    P || o.goErrorMsg({}, !0), P = P ? JSON.parse(P) : {};
    var k = p.cache.cookie, O = m.get(k.brand_name.key);
    e.advertId = n.advert_id, e.channelId = n.channel_id, e.efs_channel_id = n.efs_channel_id, e.isCompanyShow = !1, e.selectList = [], e.scrollList = [];
    var D = m.get(k.invite_union_id.key), U = m.get(k.mgm_activity_id.key), M = void 0,
        R = ["56秒前", "1分钟前", "3分钟前", "5分钟前", "8分钟前", "10分钟前", "15分钟前", "半小时前", "一小时前", "两小时前"];
    e.input = {
        brandName: new s.Input({
            lab: "所属品牌",
            val: O,
            placeholder: "请选择",
            containerClass: "",
            warningClass: "opt-warning",
            inputClass: "part-con",
            labelClass: "part-title",
            itemClass: "input-part",
            maxlength: 50,
            validation: {
                validator: function (e) {
                    return "" != e && "" != e.trim()
                }, err: "所属品牌不能为空"
            },
            blur: function () {
                S.setTopValue()
            }
        }),
        receiverMobile: new s.InputMobile({
            lab: "联系人手机号",
            val: "",
            placeholder: "请输入",
            containerClass: "",
            warningClass: "opt-warning",
            inputClass: "part-con",
            labelClass: "part-title",
            itemClass: "input-part",
            blur: function () {
                S.setTopValue()
            }
        }),
        enterprisName: new s.Input({
            lab: "",
            val: "",
            placeholder: "请输入营业执照上企业全称",
            containerClass: "",
            inputClass: "part-con",
            labelClass: "",
            itemClass: "part-enterpris",
            maxlength: 50,
            validation: {
                validator: function (e) {
                    return void 0 != e && "" != e && "" != e.trim()
                }, err: "企业名称不能为空"
            },
            change: function () {
                var t = e.input.enterprisName.val.length;
                if (M && h.cancel(M), e.input.enterprisName.val || (e.isCompanyShow = !1), t > 0) {
                    var n = [], o = {data: {root: e.input.enterprisName.val}};
                    e.input.receiverMobile.val && r.isValidPhoneNumber(e.input.receiverMobile.val) && (M = h(function () {
                        a.http(angular.extend(o, i.advertisement.getAssociatedNames)).then(function (t) {
                            n = t.data.list, n.length > 0 && (e.selectList = n, e.isCompanyShow = !0)
                        })
                    }, 400)), e.onItemDown = function (e) {
                        event.preventDefault()
                    }, e.onItemClick = function (t) {
                        e.isCompanyShow = !1, e.input.enterprisName.val = n[t]
                    }
                } else e.isCompanyShow = !1
            },
            blur: function () {
                e.isCompanyShow = !1, S.setTopValue()
            }
        }),
        contactName: new s.Input({
            lab: "法人代表姓名",
            val: "",
            placeholder: "请填写",
            containerClass: "",
            warningClass: "opt-warning",
            inputClass: "part-con",
            labelClass: "part-title",
            itemClass: "input-part",
            maxlength: 50,
            validation: {
                validator: function (e) {
                    return void 0 != e && "" != e && "" != e.trim()
                }, err: "法人代表不能为空"
            },
            blur: function () {
                S.setTopValue()
            }
        }),
        otp: new s.InputOTP({
            lab: "验证码",
            placeholder: "请输入验证码",
            containerClass: "",
            inputClass: "part-con",
            labelClass: "part-title",
            itemClass: "input-part",
            warningClass: "opt-warning",
            change: function () {
                this.validate()
            },
            blur: function () {
                S.setTopValue()
            }
        })
    }, e.btn = {
        otp: new s.ButtonOTP({
            click: function () {
                return !this.disabled() && void T()
            }
        }), submit: function () {
            N()
        }, diologPrimary: function () {
            e.diologShow = !1
        }
    }, e.onItemDown = function (e) {
        event.preventDefault()
    }, e.onItemClick = function (t) {
        e.isCompanyShow = !1, e.input.enterprisName.val = arr[t]
    }, e.choice = function () {
        m.set(k.advert_id.key, e.advertId);
        f.uc.extend(_.CACHE.BRAND_NAME, {
            enterprise_name: e.input.enterprisName.val,
            contact_name: e.input.contactName.val,
            contact_phone_no: e.input.receiverMobile.val
        }), t.path("/advertisement/brandName").search(Object.assign(n, {page_type: "asset"}))
    };
    var $ = f.uc.get(_.CACHE.ADVERTISEMENT) || {};
    $ && (e.input.enterprisName.val = $.enterprise_name, e.input.contactName.val = $.contact_name, e.input.receiverMobile.val = $.contact_phone_no), E()
}]), app.controller("marketingLoginController", ["$scope", "$routeParams", "CONSTANT", "commonConfig", "marketingService", "commonService", "feUtils", "feCookie", "feSession", "feAnalyticsService", function (e, t, n, a, i, o, r, s, c, l) {
    var u = t.activity_type, d = "Y" === t.isEsp;
    d && c.set(n.CACHE.IS_ESP, t.isEsp);
    var p = t.from;
    u || o.goErrorMsg({msg: "无法获取活动类型"}, !0), "mgmBanner" == p && l.bpButtonClick("from_mgm_banner_link");
    var m = {data: {code: r.getParamFromURL("code"), client_sys_info: r.getClientSysInfo()}};
    i.login(m, t)
}]), app.controller("marketingWeixinLoginController", ["$scope", "$routeParams", "feAlert", "apiConfig", "commonService", "feWebService", "feUtils", "marketingService", "weixinService", function (e, t, n, a, i, o, r, s, c) {
    var l = t.activity_type;
    l || i.goErrorMsg({msg: "无法获取活动类型"}, !0);
    var u = o.getLocalFileAbsUrl(a.activity.marketingLoginRoute);
    u = r.appendParamsToRouter(u, t);
    var d = c.getWeChatOAuthUrl(u);
    window.location.href = d
}]), app.controller("achievementWallController", ["$scope", "$location", "feLoading", "feSession", "feToast", "commonConfig", "feWebService", "apiConfig", "medalService", function (e, t, n, a, i, o, r, s, c) {
    function l() {
        u()
    }

    function u() {
        r.http(s.medal.getMedalList).then(function (t) {
            var n = t.data || {}, a = n.medal_list || [];
            n.have_num = n.have_num || 0, n.total_num = n.total_num || 0, n.lightAll = n.have_num === n.total_num, a.forEach(function (e) {
                c.formatMedalInfo(e)
            }), e.medalsInfo = n
        })
    }

    function d(e) {
        try {
            var t = document.createElement("canvas");
            t.width = e.width, t.height = e.height;
            var n = t.getContext("2d");
            return n.drawImage(e, 0, 0, e.width, e.height), t.toDataURL("image/png")
        } catch (e) {
        }
    }

    var p = "/cpmm-amfront/medal/shareImg.json", m = "";
    l(), e.showMedalInfo = function (e) {
        a.set(o.cache.session.medal_info.key, JSON.stringify(e)), t.path("/medal/medalInfo")
    }, e.achieveWallDialogInfo = {
        showPrizeInfo: !1, show: function () {
            n.show();
            var t = new Image;
            t.onload = function () {
                m = d(t), n.hide(), m && (e.achieveWallDialogInfo.dialogStyle = {
                    width: t.width / 2.2 + "px",
                    height: t.height / 2.2 + "px"
                }, e.achieveWallDialogInfo.imgSrc = m, e.achieveWallDialogInfo.showPrizeInfo = !0, e.$apply())
            }, t.onerror = function () {
                n.hide(), i.show("生成成就墙失败，请稍候再试~"), e.$apply()
            }, t.src = p
        }, imgSrc: p, dialogStyle: {}
    }
}]), app.factory("medalService", ["$location", "CONSTANT", "feUtils", "envConfig", "feFloatUtil", function (e, t, n, a, i) {
    function o() {
        var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {},
            n = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : "list", a = "Y" === e.user_have_flag;
        e.isLight = a, a ? e.lightImg = "list" === n ? e.img_small_bright_url : e.img_big_bright_url : e.unLightImg = "list" === n ? e.img_small_gray_url : e.img_big_gray_url, e.strategyList = d(e), e.hasStrategyList = e.strategyList.length > 0, r(e);
        var i = e.strategy_status === t.MEDAL.STRATEGY_STATUS.NOT_STARTED ? e.strategy_begin_time : e.strategy_end_time;
        e.strategyTime = u(i), e.statusImg = !a && E[e.strategy_status], e.showStrategyTime = !!e.showStrategyTime && !(e.hasStrategyList && e.btnInfo && e.btnInfo.show)
    }

    function r(e) {
        var a, i = !e.isLight && e.strategy_status === t.MEDAL.STRATEGY_STATUS.IN_PROGRESS,
            o = (a = {}, _defineProperty(a, t.MEDAL.MEDAL_THIRD_TYPE.STEP_ENTERPRISE_VERIFY, function () {
                return {medal_tips: s(e, {progress: _, notStartTip: _}), btnInfo: p(e)}
            }), _defineProperty(a, t.MEDAL.MEDAL_THIRD_TYPE.STEP_FIRST_CREDIT_SUCC, function () {
                return {
                    medal_tips: s(e, {progress: v}),
                    btnInfo: p(e),
                    showStrategyTime: !e.isLight && e.strategy_status === t.MEDAL.STRATEGY_STATUS.CLOSED
                }
            }), _defineProperty(a, t.MEDAL.MEDAL_THIRD_TYPE.STEP_FIRST_LOAN_SUCC, function () {
                return {medal_tips: s(e, {progress: h}), btnInfo: p(e)}
            }), _defineProperty(a, t.MEDAL.MEDAL_TYPE.MGM, function () {
                return e.strategyList.push({
                    award_type: "P",
                    point_num: e.recommend_succ_point,
                    urlType: "MGM_MALL"
                }), e.hasStrategyList = !1, {
                    medal_tips: s(e, {
                        getTip: "您已成功邀请" + e.recommend_succ_num + "位好友",
                        progress: C
                    }, "MGM"),
                    btnInfo: {
                        show: e.isLight || e.strategy_status !== t.MEDAL.STRATEGY_STATUS.CLOSED,
                        disabled: e.strategy_status === t.MEDAL.STRATEGY_STATUS.NOT_STARTED
                    },
                    mgmStrategyList: d(e),
                    strategyList: []
                }
            }), _defineProperty(a, t.MEDAL.MEDAL_TYPE.SUM_LOAN_AMT, function () {
                return {
                    progressInfo: {
                        show: i,
                        progressText: l(e.sum_loan_amt) + "万",
                        progress: n.toPercent(e.sum_loan_amt, e.pass_sum_loan_amt)
                    },
                    medal_tips: s(e, {
                        progress: "再借" + l(Math.max(e.pass_sum_loan_amt - e.sum_loan_amt, 0)) + "万即可获得勋章",
                        notStartTip: "借款" + l(e.pass_sum_loan_amt) + "万即可获得勋章"
                    }, "DELAY"),
                    btnInfo: p(e)
                }
            }), _defineProperty(a, t.MEDAL.MEDAL_TYPE.SUM_LOAN_DAYS, function () {
                return {
                    progressInfo: {
                        show: i,
                        progressText: e.sum_loan_days + "天",
                        progress: n.toPercent(e.sum_loan_days, e.pass_sum_loan_days)
                    },
                    btnInfo: {show: !1},
                    medal_tips: s(e, {
                        progress: "您已累计用款" + e.sum_loan_days + "天，再正常借款" + Math.max(e.pass_sum_loan_days - e.sum_loan_days, 0) + "天且不提前还款，即可获得勋章",
                        notStartTip: "累计用款" + e.pass_sum_loan_days + "天不提前还款，获得勋章"
                    }, "DELAY")
                }
            }), _defineProperty(a, t.MEDAL.MEDAL_TYPE.USED_CREDIT_AMT_RATE, function () {
                var t = e.pass_used_credit_amt_rate, n = e.total_credit_amt * t, a = Math.max(0, n - e.used_credit_amt),
                    o = n <= 0 ? 0 : e.used_credit_amt / n,
                    r = "您当前用款" + l(e.used_credit_amt) + "万，再借" + l(a) + "万即可获得勋章";
                return e.total_credit_amt <= 0 && (r = "您还未核额成功", e.button_text = "去申请核额", i = !1), {
                    medal_tips: s(e, {
                        progress: r,
                        notStartTip: "额度使用率达" + 100 * Number(t) + "%即可获得勋章"
                    }, "DELAY"),
                    btnInfo: p(e),
                    progressInfo: {show: i, progress: Math.min(100, Math.floor(100 * o)) + "%"}
                }
            }), _defineProperty(a, t.MEDAL.MEDAL_TYPE.ONE_LOAN_KEEP_DAYS, function () {
                return {medal_tips: c(e)}
            }), a), r = "STEP" === e.second_type ? e.third_type : e.second_type, u = o[r] || function () {
                return {}
            }, m = u();
        angular.extend(e, m), e.medalType = r
    }

    function s(e, n) {
        var a = arguments.length > 2 && void 0 !== arguments[2] ? arguments[2] : "STEP", i = n.progress,
            o = n.notStartTip || i, r = n.getTip || f, s = e.isLight, c = S[a], l = "", u = "", d = "";
        return s ? l = r : e.strategy_status === t.MEDAL.STRATEGY_STATUS.CLOSED ? (u = b, l = g, d = c.CLOSED) : e.strategy_status === t.MEDAL.STRATEGY_STATUS.IN_PROGRESS ? (u = w, d = c.IN_PROGRESS, l = i) : (u = y, l = o), e.showStrategyTime = !s, e.strategyTimeTips = u, e.medalTipText = d, l
    }

    function c(e) {
        var a = S.DELAY, i = e.strategy_status, o = e.curr_loan_keep_days, r = e.pass_one_loan_keep_days,
            s = e.strategy_end_time, c = e.local_time, l = t.MEDAL.STRATEGY_STATUS, d = u(e.strategy_end_time), p = {
                show: !1,
                progressText: e.pass_one_loan_keep_days + "天",
                progress: n.toPercent(e.curr_loan_keep_days, e.pass_one_loan_keep_days)
            }, m = {
                hasLoan: "您已连续使用" + o + "天，再坚持使用" + Math.max(r - o, 0) + "天且不提前还款，即可获得勋章",
                needLoan: "借一笔" + r + "天的借款不提前还款即可获得勋章",
                noEligible: "获得勋章需要在" + d.year + "年" + d.month + "月" + d.day + "日前完成连续" + r + "天的借款，您暂不符合资格",
                unStart: "单笔借款连续用款" + r + "天不提前还款且不逾期即可获得勋章"
            }, _ = "", v = !1, h = "", C = "";
        return e.isLight ? _ = f : i === l.CLOSED ? (v = !0, _ = g, h = b, C = a.CLOSED) : i === l.NOT_STARTED ? (v = !0, _ = m.unStart, h = y) : i === l.IN_PROGRESS && (C = a.IN_PROGRESS, e.loan_no ? (_ = m.hasLoan, e.showLoanNo = !0, p.show = !0) : new Date(s).getTime() - new Date(c).getTime() > 24 * r * 3600 * 1e3 ? (v = !0, e.btnInfo = {
            show: !0,
            disabled: !1
        }, _ = m.needLoan, h = w, p.show = !0) : _ = m.noEligible), e.strategyTimeTips = h, e.showStrategyTime = v, e.medalTipText = C, e.progressInfo = p, _
    }

    function l(e) {
        return i.toFixed(Number(e) / 1e4, 2, !1)
    }

    function u(e) {
        var t = new Date(e), n = t.getFullYear(), a = t.getMonth() + 1, i = t.getDate(), o = t.getHours(),
            r = t.getMinutes();
        return a = a > 9 ? a : "0" + a, i = i > 9 ? i : "0" + i, o = o > 9 ? o : "0" + o, r = r > 9 ? r : "0" + r, {
            year: n,
            month: a,
            day: i,
            hours: o,
            minutes: r
        }
    }

    function d() {
        var t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {}, n = "Y" === t.user_have_flag,
            i = (n ? t.user_award_list : t.strategy_award_list) || [];
        return i.forEach(function (t) {
            "C" === t.award_type ? (t.text = t.coupons_name, t.class = "coupon", t.click = function () {
                e.path("/coupon/list")
            }) : "P" === t.award_type && (t.text = t.point_num + "积分", t.class = "point", t.click = function () {
                var e = {MGM_MALL: a.wzMallUrl};
                window.location.href = e[t.urlType] || a.mallUrl
            }), t.btnText = "去使用", t.btnHide = !n
        }), i
    }

    function p(e) {
        return {
            show: !e.isLight && (e.strategy_status === t.MEDAL.STRATEGY_STATUS.IN_PROGRESS || e.strategy_status === t.MEDAL.STRATEGY_STATUS.NOT_STARTED),
            disabled: e.strategy_status === t.MEDAL.STRATEGY_STATUS.NOT_STARTED
        }
    }

    var m, f = "您已获得", g = "未获得勋章", _ = "完成认证，获得勋章", v = "完成首次核额成功，获得勋章", h = "完成首次提款刷脸成功，获得勋章", C = "成功推荐好友成功，得勋章和积分",
        b = ["勋章发放已于", "结束"], y = ["勋章发放将于", "开始"], w = ["请于", "前完成该勋章任务"], S = {
            STEP: {IN_PROGRESS: "完成勋章任务后可实时获得勋章，如有延迟请耐心等待，如有积分或免息券奖励，先到先得，送完即止"},
            DELAY: {IN_PROGRESS: "当前勋章任务完成进度为截至昨日的数据，今日的完成进度请于明天查看，如有积分或免息券奖励，先到先得，送完即止", CLOSED: "当前勋章任务完成进度为截至昨日的数据"},
            MGM: {IN_PROGRESS: "完成勋章任务后可实时获得勋章，如有延迟请耐心等待"}
        },
        E = (m = {}, _defineProperty(m, t.MEDAL.STRATEGY_STATUS.NOT_STARTED, "asset/images/medal/medal_unstart.png"), _defineProperty(m, t.MEDAL.STRATEGY_STATUS.CLOSED, "asset/images/medal/medal_end.png"), m);
    return {formatMedalInfo: o}
}]), app.directive("feMedalInfo", function () {
    return {
        restrict: "AE",
        transclude: !0,
        replace: !0,
        scope: {options: "="},
        template: ' \n           <div class="medal-appeal">\n                <div class="item">\n                    <div class="item-header">\n                        <span class="item-hd-txt" ng-class="options.class">{{options.text}}</span>\n                        <button class="small-btn" ng-click="options.click()" ng-hide="options.btnHide">{{options.btnText}}</button>\n                    </div>\n<!--                    <div class="item-content" ng-transclude></div>-->\n                </div>\n            </div>',
        link: function (e) {
        }
    }
}).directive("feMedalTips", function () {
    return {
        restrict: "AE",
        transclude: !0,
        replace: !0,
        scope: {text: "="},
        template: '\n            <div class="medal-text-wrap">\n                <i class="line-left"></i>\n                <span class="medal-text" ng-transclude></span>\n                <i class="line-right"></i>\n            </div>\n            '
    }
}).directive("achieveWallDialog", function () {
    return {
        restrict: "E",
        replace: !0,
        scope: {info: "="},
        template: '\n        <div class="we-dialog" ng-if="info.showPrizeInfo">\n            <div class="we-dialog__mask"></div>\n            <div class="pop-box medal-box" ng-style="{\'width\': info.dialogStyle.width}">\n                <div class="pop-btn">\n                    <button class="save-btn">长按图片区域可保存\n                        <div class="triangle"></div>\n                    </button>\n\n                    <i class="pop-close" ng-click="closeDialog()"></i>\n                </div>\n                <div class="pop-bd">\n                    <img class="medal-post" ng-style="info.dialogStyle" ng-src="{{info.imgSrc}}"/>\n                </div>\n            </div>\n            \n        </div>\n        ',
        controller: ["$scope", "$location", "feAnalyticsService", "envConfig", function (e, t, n, a) {
            e.closeDialog = function () {
                e.info.showPrizeInfo = !1
            }
        }]
    }
}), app.controller("medalInfoController", ["$scope", "$location", "feSession", "commonConfig", "fePageService", "feWebService", "apiConfig", "medalService", "envConfig", "commonService", function (e, t, n, a, i, o, r, s, c, l) {
    function u() {
        o.http(angular.extend({data: {medal_id: p}}, r.medal.getMedalDetail)).then(function (t) {
            var n = t.data || {};
            n.local_time = n.local_time.replace(/-/g, "/"), n.strategy_begin_time = n.strategy_begin_time.replace(/-/g, "/"), n.strategy_end_time = n.strategy_end_time.replace(/-/g, "/"), s.formatMedalInfo(n, "detail"), e.medalInfo = n
        })
    }

    var d = JSON.parse(n.get(a.cache.session.medal_info.key) || ""), p = d.medal_id;
    return p ? (i.setPageTitle(d.medal_name), e.evt = {
        goReceiptDetail: function () {
            e.medalInfo.loan_no && l.goMespRoute({type: "receiptDetail", loan_acct_no: e.medalInfo.loan_no})
        }, handleClick: function () {
            var t = e.medalInfo.button_url;
            t && (window.location.href = t)
        }
    }, void u()) : void t.path("/common/msg")
}]), app.controller("medalLoginController", ["$scope", "$routeParams", "CONSTANT", "$location", "commonConfig", "marketingService", "commonService", "feUtils", "feCookie", "feSession", "apiConfig", "feWebService", function (e, t, n, a, i, o, r, s, c, l, u, d) {
    function p() {
        var e = c.get(i.cache.cookie.login_token.key), t = {data: {}},
            o = e ? angular.extend(t, u.red.getMgmUserInfo) : angular.extend(m, u.red.login);
        d.http(o).then(function (e) {
            l.set(n.CACHE.WE_PARTNER_REGISTER_STATUS, e.data.we_partner_register_status), l.set(i.cache.session.open_id.key, e.data.open_id), a.path("/medal").replace()
        }, function (e) {
            return !e || f >= 1 ? void r.goErrorMsg(e, !1) : (f++, void (r.isTokenError(e.status) && m.code ? (c.remove(i.cache.cookie.login_token.key, {path: i.cache.cookie.login_token.path}), p()) : r.goErrorMsg(e, !0)))
        })
    }

    var m = {data: {code: s.getParamFromURL("code")}}, f = 0;
    p()
}]), app.controller("medalWeixinLoginController", ["$scope", "$routeParams", "feWebService", "feUtils", "weixinService", "apiConfig", function (e, t, n, a, i, o) {
    var r = n.getLocalFileAbsUrl(o.medal.loginRoute);
    r = a.appendParamsToRouter(r, t), window.location.href = i.getWeChatOAuthUrl(r)
}]), app.controller("newLeaveRouteController", ["$scope", "$location", "$routeParams", "feWebService", "apiConfig", "commonService", "feUtils", "feUI", "feLoading", "feEnvService", "envConfig", "$http", "commonConfig", "feCookie", "feCache", "feSession", "CONSTANT", "feToast", "$timeout", "$sce", "weixinService", "marketingService", "feAnalyticsService", "leaveMessageService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h, C, b, y, w, S) {
    S.fetchPageInfo().then(function (e) {
        e ? t.path("/leavePage/" + e) : t.path("/leaveMessage")
    })
}]), app.controller("prizeListController", ["$scope", "$location", "feCache", "CONSTANT", "PageList", "envConfig", "apiConfig", "feUI", "feSession", "feAnalyticsService", "feUtils", "feCookie", "commonConfig", "commonService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m) {
    function f(e) {
        var t = e.split(" "), n = t[0].split("-"), a = n[0] + "年" + n[1] + "月" + n[2] + "日";
        return a
    }

    var g = 10, _ = function () {
        return angular.extend({data: {}}, r.red.queryAwardsList)
    }, v = {data: {}};
    l.clickStat(l.EVENT_NAME.ELEMENT_SHOW, "red_prizeListPageShow"), e.awardsList = new i({
        pageSize: g,
        requestParam: _(v),
        transformRequest: function (e) {
            return {page_start: e.pageNumber, page_size: e.pageSize}
        },
        transformResponse: function (t) {
            return t && t.award_list && t.award_list.length ? (t.award_list.forEach(function (e, t) {
                e.activity_start_time = f(e.activity_start_time), e.activity_end_time = f(e.activity_end_time), e.coupon_start_time = f(e.coupon_start_time), e.coupon_end_time = f(e.coupon_end_time)
            }), e.notEmpty = !0) : e.empty = new s.EmptyList({isEmpty: !0, title: "暂无中奖记录"}), t.award_list
        },
        errorCallback: function () {
        }
    }), e.goUse = function (n, i) {
        l.clickStat(l.EVENT_NAME.BUTTON_CLICK, "red_goToUseCoupon"), "01" == n ? t.path("/coupon/list").search({activity_type: "01"}) : "03" == n && (e.creditFlag = c.get(a.CACHE.CREDIT_FLAG), "Y" == e.creditFlag ? m.goMesp({
            cpmmEntryType: "loanApply",
            cpmmFreeCouponId: i
        }) : "N" == e.creditFlag && t.path("/red/unverifyTip"))
    }
}]), app.controller("redHomeController", ["$scope", "$routeParams", "$sce", "$location", "CONSTANT", "registerFactory", "feSession", "feAlert", "feAnalyticsService", "commonConfig", "fePageService", "feWebService", "apiConfig", function (e, t, n, a, i, o, r, s, c, l, u, d, p) {
    function m() {
        d.http(angular.extend(y, p.red.getActivityInfo)).then(function (t) {
            e.activityInfo = t.data, e.activityStatus = t.data.activity_status;
            var n = t.data.start_time, a = (t.data.end_time, t.data.countdown_days), i = t.data.countdown_seconds,
                o = n.split(" "), r = o[0].split("-");
            e.start_time = {
                month: "",
                day: ""
            }, e.start_time.month = r[1], e.start_time.day = r[2], f(a, i), u.setPageTitle(t.data.display_name), c.clickStat(c.EVENT_NAME.ELEMENT_SHOW, "red_redHomePageShow"), C && (g(), C = !1)
        })
    }

    function f(t, n) {
        var a = 3600 * t * 24 + n;
        if ("4" == e.activityStatus) {
            if (!(a > 0)) return;
            var i = setInterval(function () {
                e.$apply(function () {
                    if (a < 0) m(), i && (clearInterval(i), i = null); else {
                        var t = a;
                        e.else_time = {
                            hour: {first: "", second: "", third: ""},
                            minute: {first: "", second: ""},
                            seconds: {first: "", second: ""}
                        };
                        var n = Math.floor(t / 3600);
                        e.else_time.hour.first = Math.floor(n / 100), e.else_time.hour.second = Math.floor(n % 100 / 10), e.else_time.hour.third = n % 100 % 10, t %= 3600;
                        var o = Math.floor(t / 60);
                        e.else_time.minute.first = Math.floor(o / 10), e.else_time.minute.second = o % 10, t %= 60;
                        var r = t % 60;
                        e.else_time.seconds.first = Math.floor(r / 10), e.else_time.seconds.second = r % 10, a -= 1
                    }
                })
            }, 1e3)
        } else "3" == e.activityStatus && setTimeout(function () {
            e.$apply(function () {
                m()
            })
        }, 1e3 * (a + 5))
    }

    function g() {
        v();
        var t = e.activityInfo.activity_rule_desc || "";
        r.set(i.CACHE.RULE_DESC, t), e.ruleInfo = {
            showRules: !1,
            activity_desc: n.trustAsHtml(t.replace(/\n/gi, "<br>")),
            show: function () {
                e.ruleInfo.showRules = !0
            }
        }, e.prizeInfo = {
            showPrizeInfo: !1, show: function () {
                var t = r.get(i.CACHE.WE_PARTNER_REGISTER_STATUS);
                c.clickStat(c.EVENT_NAME.BUTTON_CLICK, "red_redOpen"), t && "0" != t ? e.activityInfo.remain_num > 0 ? d.http(angular.extend(y, p.red.drawAward)).then(function (t) {
                    if (e.prizeInfo.data = t.data, e.prizeInfo.creditFlag = r.get(i.CACHE.CREDIT_FLAG), t.data) {
                        e.prizeInfo.goods_name = e.prizeInfo.data.goods_name, e.prizeInfo.data.draw_status = t.data.draw_status.toUpperCase();
                        var n = e.prizeInfo.data.draw_status;
                        t.data.remain_num >= 0 && null != t.data.remain_num && ("S" != n && "F1" != n || (e.activityInfo.remain_num = t.data.remain_num)), "F3" == n ? s.show({
                            class: "red-packet",
                            content: "抱歉，本活动目前仅对符合活动规则的用户开放！",
                            btn: {text: "我知道了"}
                        }) : "F4" == n ? s.show({
                            class: "red-packet",
                            content: "您的抽奖次数已经用完！",
                            btn: {text: "我知道了"}
                        }) : "S" == n || "F1" == n || "F2" == n ? e.prizeInfo.showPrizeInfo = !0 : s.show({
                            class: "red-packet",
                            content: "活动不可用！",
                            btn: {text: "我知道了"}
                        })
                    } else s.show({class: "red-packet", content: "活动不可用！", btn: {text: "我知道了"}})
                }) : s.show({
                    class: "red-packet",
                    content: "您的抽奖次数已经用完！",
                    btn: {text: "我知道了"}
                }) : e.register.showDialog = !0
            }
        }, e.register = new o.initRegister
    }

    function _() {
        a.path("/red/prizeList")
    }

    function v() {
        var e = r.get(i.CACHE.WE_PARTNER_REGISTER_STATUS);
        e && "0" != e && d.http(angular.extend({
            data: {
                query_credit_flag: "1",
                scene_id: "red_packet"
            }
        }, p.red.getUserInfo)).then(function (e) {
            r.set(i.CACHE.CREDIT_FLAG, e.data.credit_flag)
        })
    }

    function h(t, n) {
        var a = r.get(i.CACHE.WE_PARTNER_REGISTER_STATUS);
        a && "0" != a ? t && "function" == typeof t && t(n) : e.register.showDialog = !0
    }

    var C = !0, b = t.activityId, y = {data: {activity_id: b}};
    m(), e.goRecordList = function () {
        h(_)
    }
}]), app.controller("unverifyTipController", ["$scope", "$routeParams", "$sce", "$location", "commonService", "feUI", "CONSTANT", "feAnalyticsService", "registerFactory", "feSession", "envConfig", "feUtils", "feWebService", "feCookie", "commonConfig", "apiConfig", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g) {
    function _() {
        s.clickStat(s.EVENT_NAME.ELEMENT_SHOW, "red_unverifyPageShow");
        var t = l.get(r.CACHE.RULE_DESC);
        e.ruleInfo = {
            showRules: !1, activity_desc: n.trustAsHtml(t.replace(/\n/gi, "<br>")), show: function () {
                e.ruleInfo.showRules = !0
            }
        }
    }

    function v() {
        a.path("/red/prizeList")
    }

    function h(t, n) {
        var a = l.get(r.CACHE.WE_PARTNER_REGISTER_STATUS);
        a && "0" != a ? t && "function" == typeof t && t(n) : e.register.showDialog = !0
    }

    _(), e.goRecordList = function () {
        h(v)
    }, e.goToApply = function () {
        s.clickStat(s.EVENT_NAME.ROUTE_CHANGE, "red_goLoanApply_unverifyPage"), i.goMesp({cpmmEntryType: "cpmmEntry"})
    }
}]), app.controller("redLoginController", ["$scope", "$routeParams", "CONSTANT", "commonConfig", "marketingService", "commonService", "feUtils", "feCache", "feCookie", "feSession", "feWebService", "apiConfig", "$location", function (e, t, n, a, i, o, r, s, c, l, u, d, p) {
    function m() {
        var e = c.get(a.cache.cookie.login_token.key), t = {data: {query_credit_flag: "1", scene_id: "red_packet"}},
            i = e ? angular.extend(t, d.red.getMgmUserInfo) : angular.extend(f, d.red.login);
        u.http(i).then(function (e) {
            l.set(n.CACHE.WE_PARTNER_REGISTER_STATUS, e.data.we_partner_register_status), l.set(a.cache.session.open_id.key, e.data.open_id), p.path("/red").replace()
        }, function (e) {
            return !e || g >= 1 ? void o.goErrorMsg(e, !1) : (g++, void (o.isTokenError(e.status) && f.code ? (c.remove(a.cache.cookie.login_token.key, {path: a.cache.cookie.login_token.path}), m()) : o.goErrorMsg(e, !0)))
        })
    }

    var f = {data: {code: r.getParamFromURL("code"), scene_id: "red_packet"}}, g = 0;
    m()
}]), app.controller("redWeixinLoginController", ["$scope", "$routeParams", "apiConfig", "feWebService", "feUtils", "commonConfig", "commonService", "weixinService", function (e, t, n, a, i, o, r, s) {
    var c = a.getLocalFileAbsUrl(n.red.loginRoute);
    c = i.appendParamsToRouter(c, t), window.location.href = s.getWeChatOAuthUrl(c)
}]), app.controller("applyProcessController", ["$scope", "$routeParams", "$location", "feUtils", "feWebService", "marketingService", "apiConfig", "weixinService", "$sce", "feEnvService", "envConfig", "commonConfig", "feUI", "feCookie", "CONSTANT", "feTips", "feConfirm", "commonService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v) {
    function h() {
        var e = d.cache.cookie, n = e.mgm_activity_id, a = e.invite_union_id, i = e.agree_register_wepartner,
            o = !!location.href.startsWith("https");
        m.set(n.key, t.mgmActivityId, {path: n.path, secure: o}), m.set(a.key, t.inviteUnionId, {
            path: a.path,
            secure: o
        }), m.set(i.key, "Y", {path: i.path, secure: o}), window.location.href = u.mespUrl
    }

    t.inviteUnionId && t.mgmActivityId || v.goErrorMsg({}, !0), e.button = {
        applyBtn: new p.Button({
            text: "开始申请",
            waValue: "mgm_applyProcess_applyBtn",
            click: function () {
                d.cache.cookie;
                h()
            }
        })
    }
}]), app.controller("inviteRegisterController", ["$scope", "$routeParams", "$location", "feUtils", "feWebService", "marketingService", "apiConfig", "weixinService", "$sce", "feEnvService", "envConfig", "commonConfig", "feUI", "feCookie", "CONSTANT", "feTips", "feConfirm", "commonService", "feAnalyticsService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g, _, v, h) {
    function C() {
        n.path("/mgm/applyProcess").search({mgmActivityId: t.mgmActivityId, inviteUnionId: t.inviteUnionId})
    }

    e.showPage = !1, t.inviteUnionId && t.mgmActivityId || v.goErrorMsg({}, !0), h.bpButtonClick("from_invite_register_link");
    var b = a.getParamFromURL("code");
    if (!b) return void (window.location.href = s.getWeChatOAuthUrl(window.location.href));
    var y = {data: {code: a.getParamFromURL("code"), client_sys_info: a.getClientSysInfo()}};
    o.login(y, {activity_type: "01"}, function (n) {
        var a = {data: {activity_id: t.mgmActivityId, aes: "Y"}};
        i.http(angular.extend(a, r.activity.getActivityInfo)).then(function (t) {
            e.showPage = !0;
            var n = t.data;
            e.activityInfo = n, e.isEnd = n.is_end == f.APP.ACTIVITY_IS_END.IS_END, e.isClosed = n.activity_status == f.APP.MGM_ACTIVITY_STATUS.CLOSED, e.activityInfo.activity_rule_desc = c.trustAsHtml(n.activity_rule_desc.replace(/\\n/g, "<br>")), e.showRulesDialog = function () {
                e.activityInfo.showRules = !0
            }
        }, function (e) {
            v.goErrorMsg(e, !0)
        })
    }), e.button = {
        agreeInvitedBtn: new p.Button({
            text: "接受邀请",
            waValue: "mgm_inviteRegister_agreeInvitedBtn",
            click: function () {
                d.cache.cookie;
                C()
            }
        }), goRegister: new p.Button({
            text: "推荐有礼", waValue: "mgm_inviteRegister_goRegister", click: function () {
                n.path("/marketing/login").search({activity_type: "01"})
            }
        })
    }
}]), app.controller("registerController", ["$scope", "mgmUI", "feUI", "$location", "feUtils", "feCookie", "feAlert", "$routeParams", "$sce", "marketingService", "weixinService", "feAnalyticsService", "commonConfig", "apiConfig", "feWebService", "commonService", function (e, t, n, a, i, o, r, s, c, l, u, d, p, m, f, g) {
    function _() {
        var t = e.register.phoneNo;
        return i.isValidPhoneNumber(t) ? (e.register.otp.countdown(" s", "发送验证码"), void f.http(angular.extend({data: {mobileNo: t}}, m.register.getMgmOtp)).then(function (e) {
        })) : void (e.register.errMsg = p.msg.mobileNumError)
    }

    function v() {
        e.register.errMsg = "";
        var t = e.register.phoneNo, n = e.register.authCode;
        if (!i.isValidPhoneNumber(t)) return void (e.register.errMsg = p.msg.mobileNumError);
        if (!n) return e.register.errMsg = p.msg.otpCodeNULLError, void (e.register.submitBtnDisabled = !0);
        var a = {data: {phone_no: t, otp_code: n}};
        f.http(angular.extend(a, m.register.mgmRegister)).then(function (t) {
            var n = t.data;
            0 == n.status ? h() : 1 == n.status ? e.register.errMsg = p.msg.sysBusyError : 2 == n.status ? e.register.errMsg = p.msg.mobileAreadyRegister : 3 == n.status ? e.register.errMsg = p.msg.authCodeErr : e.register.errMsg = p.msg.sysBusyError
        })
    }

    function h() {
        var e = o.get(p.cache.cookie.login_token.key);
        if (e) {
            var t = s.activity_type;
            l.login({}, {activity_type: t})
        } else r.show({
            content: p.msg.tokenOutTime, btn: {
                text: "确定", click: function () {
                    u.invoke("closeWindow")
                }
            }
        })
    }

    e.register = new t.MgmRegister({
        showDialog: !1,
        submitBtnDisabled: !0,
        otp: {
            text: "发送验证码", waValue: "mgm_register_otpBtn", click: function () {
                return !this.disabled() && void _()
            }
        },
        submit: function () {
            d.bpButtonClick("mgm_register_submitBtn"), v()
        }
    }), e.activityInfo = {};
    var C = {data: {code: i.getParamFromURL("code"), client_sys_info: i.getClientSysInfo()}};
    l.login(C, {activity_type: "01"}, function (t) {
        var n = t.mgm_activity ? t.mgm_activity : t.current_activity, a = n.activity_rule_desc || "";
        e.showRulesDialog = function () {
            e.activityInfo.showRules = !0
        }, e.activityInfo.activity_rule_desc = c.trustAsHtml(a.replace(/\\n/g, "<br>"))
    }), e.joinWeBanBtn = function () {
        d.bpButtonClick("mgm_register_joinWeBanBtn"), e.register.showDialog = !0
    }
}]), app.controller("telemarketing", ["$scope", "feWebService", "apiConfig", "$routeParams", "envConfig", function (e, t, n, a, i) {
    var o = i.cdnPrefix, r = {data: {srcId: a.srcId}};
    t.http(angular.extend(r, n.telemarketing.qrySourceIdFromMsg)).then(function (t) {
        t && t.data && (e.imgUrl = o + t.data.cdnPath)
    })
}]);
//# sourceMappingURL=../../../rev/app.js.map"use strict";function _toConsumableArray(e){if(Array.isArray(e)){for(var t=0,n=Array(e.length);t<e.length;t++)n[t]=e[t];return n}return Array.from(e)}function _defineProperty(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function _defineProperty(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}var _typeof="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e};self===top?document.body.style.display="block":top.location=self.location,"addEventListener"in document&&document.addEventListener("DOMContentLoaded",function(){"undefined"!=typeof FastClick&&FastClick.attach(document.body)},!1),FastClick.prototype.focus=function(e){var t=void 0;e.setSelectionRange&&0!==e.type.indexOf("date")&&"time"!==e.type&&"month"!==e.type?(t=e.value.length,e.focus(),e.setSelectionRange(t,t)):e.focus()};var wbTmpl=angular.module("wbTmpl",[]),app=angular.module("app",["ngRoute","wbTmpl","feBase","infinite-scroll","monospaced.qrcode"]);app.run(["$rootScope","$http","$location","browserService","feEnvService","feWebService","fePageService","weixinService","CONSTANT","feUtils","feCache","feActionSheet","feAnalyticsService","feConfirm","feAlert","fePwd","feLoading","feQRCode","feTips","demandLoadingService","envConfig","commonConfig",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h,C,b,y){function w(){"object"===("undefined"==typeof wx?"undefined":_typeof(wx))&&wx.hideAllNonBaseMenuItem()}function S(){var e=document.getElementsByClassName("fe-confirm");e&&e.length&&m.hide();var t=document.getElementsByClassName("fe-alert");t&&t.length&&f.hide();var n=document.getElementsByClassName("mod-actionsheet");n&&n.length&&d.hide();var a=document.getElementsByClassName("fe-pwd");a&&a.length&&g.hide();var i=document.getElementsByClassName("fe-loading");i&&i.length&&_.hide();var o=document.getElementsByClassName("gearDate");o&&o.length&&(gearDate.outerHTML="");var r=document.getElementsByClassName("mod-tips");r&&r.length&&h.hide();var s=document.getElementsByClassName("qr-code");s&&s.length&&v.hide()}function E(){var t=e.analytics;if(!(t.routeChangeStartTime&&t.routeChangeEndTime&&t.routePath))return void(t.routeChangeStartTime=(new Date).getTime());t.routeChangeStartTime=(new Date).getTime();var n=t.routeChangeStartTime-t.routeChangeEndTime;/^\/\w+\/?(\/\w+)?$/.test(t.routePath)&&p.pageStayTime(n,t.routePath)}i.initEnv(b),e.hotline=c.APP.HOTLINE,e.analytics={},e.$on("$routeChangeStart",function(e,t){var a=t&&t.$$route;if(a&&a.demandLoadArr&&C.load(a.demandLoadArr),"all"!==a.openType&&!l.isWechat())return u.uc.extend(c.CACHE.COMMON_MSG,{title:"提示",content:y.msg.userWeixinBrowser}),void n.path("/common/msg");E();var a=t&&t.$$route;a&&S()}),e.$on("$routeChangeSuccess",function(t,n,i,o){var s=n&&n.$$route;s&&(s.pageTitle&&r.setPageTitle(s.pageTitle),a.filter(s),e.analytics.routePath=s.originalPath),w(),e.analytics.routeChangeEndTime=(new Date).getTime()}),document.onreadystatechange=function(){var e=document.readyState,t=window.indexLoadStartTime;if(t&&"complete"===e){var n=(new Date).getTime();p.bpRouteChange("index.html",{loadTime:n-t})}},window.onbeforeunload=E}]),app.factory("$exceptionHandler",["$log","feAnalyticsService",function(e,t){return function(n,a){e.warn(n,a),t.errorReport(n)}}]),app.controller("historyListController",["$scope","$rootScope","$location","CONSTANT","PageList","apiConfig","feUI",function(e,t,n,a,i,o,r){e.historyList=new i({requestParam:o.activity.getHistoricalActivities,transformResponse:function(t){return t&&t.list&&t.list.length?e.notEmpty=!0:e.empty=new r.EmptyList({isEmpty:!0,title:"暂未发布任何活动",iconClass:"ui-icon-banner-empty"}),t.list},errorCallback:function(){}}),e.goHistoryDetail=function(e){return e.activity_status==a.APP.MGM_ACTIVITY_STATUS.CLOSED?void n.path("/mgm/noActivity"):void n.path("/mgm/home").search({activityId:e.activity_id})}}]),app.controller("homeController",["$scope","$rootScope","$timeout","$interval","$routeParams","feLoading","$location","feTips","feUtils","$sce","feCookie","feAnalyticsService","CONSTANT","feCONSTANT","feUI","apiConfig","commonConfig","feQRCode","marketingService","feCache","feWebService","commonService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h,C,b,y){function w(){e.activityInfo={showHomeUpBtn:!0,isEnd:!1,goInvitedListPage:function(){d.bpButtonClick("mgm_home_goInvitedListBtn"),r.path("/mgm/invitedList")},goExchangedListPage:function(){d.bpButtonClick("mgm_home_goExchangeListBtn"),r.path("/mgm/exchangedList").search({activityId:i.activityId})},goGiftExchangePage:function(){d.bpButtonClick("mgm_home_goGiftExchangePageBtn"),r.path("/mgm/giftExchange").search({activityId:i.activityId})},showAwardCoinsDesc:function(){s.show({title:"奖励金",content:"获得奖励金后可在微业伴活动页兑换等值奖品，若已为企业金融老用户则不发放奖励金（完成过认证授权即为老客户）。"})},nickname:u.get(_.cache.cookie.nickname.key)||"",headimgurl:u.get(_.cache.cookie.headimgurl.key)||""},e.goHistoryPage=function(){d.bpButtonClick("mgm_home_goHistoryPageBtn"),r.path("/mgm/historyList")},e.inviteBtn=new f.Button({text:"立即邀请",waValue:"mgm_home_inviteBtn",disabled:!1,click:function(){u.set(T.nickname.key,e.activityInfo.nickname,{path:T.nickname.path,secure:A}),h.showMgmShareMenu()}}),e.showRulesDialog=function(){e.activityInfo.showRules=!0}}function S(t){u.set(T.agree_register_award_coins.key,t.cover_inviter_award_coins,{path:T.agree_register_award_coins.path,secure:A}),u.set(T.mgm_activity_id.key,t.activity_id,{path:T.mgm_activity_id.path,secure:A}),e.activityInfo=angular.extend(e.activityInfo,t);var n={data:{activity_id:E,pageNumber:1,pageSize:m.PAGE.PAGESIZE}};e.activityInfo.activity_rule_desc=l.trustAsHtml(e.activityInfo.activity_rule_desc.replace(/\\n/g,"<br>")),e.activityInfo.award_coins_expire_time=c.formatDate(e.activityInfo.award_coins_expire_time),b.http(angular.extend(n,g.gift.getAvailableAwards)).then(function(t){var n=t.data.list||[];n.length>3&&(n=n.splice(0,3)),e.activityInfo.imgList=n})}w();var E=i.activityId,T=_.cache.cookie,A=y.getCookieSecure();if(E){var N={data:{activity_id:E}};b.http(angular.extend(N,g.activity.getActivityInfo)).then(function(t){var n=t.data;n.is_end==p.APP.ACTIVITY_IS_END.IS_END&&(e.activityInfo.isEnd=!0,e.inviteBtn.disabled=!0),S(n)},function(t){e.inviteBtn.disabled=!0,e.activityInfo.isNetWorkErr=!0})}else y.goErrorMsg({},!0)}]),app.controller("invitedListController",["$scope","$location","commonConfig","feCookie","$routeParams","feUI","apiConfig","feUtils","CONSTANT","PageList","feQRCode","marketingService",function(e,t,n,a,i,o,r,s,c,l,u,d){var p=n.cache.cookie,m=a.get(p.mgm_activity_id.key);angular.extend(r.activity.getInvitedCorps,{data:{activity_id:m}}),e.isEnd=!1,e.invitedList=new l({requestParam:r.activity.getInvitedCorps,transformResponse:function(t){t&&t.list&&t.list.length?(e.invitedSize=t.total,e.notEmpty=!0):e.empty=new o.EmptyList({isEmpty:!0,title:"当前没有邀请记录",iconClass:" ",desc:"快去邀请小伙伴们加入<br>即可获得奖励金兑换奖品"}),t.is_end&&"Y"===t.is_end&&(e.isEnd=!0);var n=!0,a=!1,i=void 0;try{for(var r,c=(t.list||[])[Symbol.iterator]();!(n=(r=c.next()).done);n=!0){var l=r.value;l.invite_enterprise_time=s.formatDate(l.invite_enterprise_time)}}catch(e){a=!0,i=e}finally{try{!n&&c.return&&c.return()}finally{if(a)throw i}}return t.list}}),e.inviteBtn=new o.Button({text:"立即邀请",waValue:"mgm_invitedList_inviteBtn",click:function(){d.showMgmShareMenu()}})}]),app.controller("noActivityController",["$scope",function(e){}]),app.controller("manageAddressController",["$scope","$location","CONSTANT","apiConfig","PageList","feCache","feUI","feConfirm","feWebService","exchangeDetailService",function(e,t,n,a,i,o,r,s,c,l){var u=o.uc.get(n.CACHE.ADDRESS);e.button={addAddress:new r.Button({text:"新增地址",click:function(){o.uc.remove(n.CACHE.ADDRESS),t.path("/address/editAddress").search({type:1})}}),confirm:new r.Button({text:"确定",click:function(){l.setChooseAddress(u),o.uc.extend(n.CACHE.ADDRESS,u),t.path("/mgm/exchangeDetail")}})},e.check=function(t){u&&(u.checked=!1),e.addressList.items[t].checked=!0,u=e.addressList.items[t]},e.delete=function(t){s.show({content:"确定删除该地址？",btn:{primary:{text:"确定",click:function(){s.hide();var i={data:{address_id:e.addressList.items[t].address_id}};c.http(angular.extend(i,a.address.delAddress)).then(function(){e.addressList.items[t].address_id==u.address_id&&(u=null);var a=l.getChooseAddress();null!=a&&a.address_id&&l.setChooseAddress(null),o.uc.extend(n.CACHE.ADDRESS,u),e.addressList.refresh()})}}}})},e.edit=function(a){o.uc.extend(n.CACHE.ADDRESS,e.addressList.items[a]),t.path("/address/editAddress")};var d=function(){e.addressList=new i({requestParam:a.address.getAddressList,pageSize:5,transformResponse:function(t){null==t.list||0==t.list.length?(e.button.confirm.show=!1,e.empty=new r.EmptyList({isEmpty:!0,title:"地址列表为空",iconClass:"ui-icon-banner-empty"})):e.notEmpty=!0;var n=!0,a=!1,i=void 0;try{for(var o,s=(t.list||[])[Symbol.iterator]();!(n=(o=s.next()).done);n=!0){var c=o.value;u?c.address_id==u.address_id?(u=c,c.checked=!0):c.checked=!1:(u=c,c.checked=!0)}}catch(e){a=!0,i=e}finally{try{!n&&s.return&&s.return()}finally{if(a)throw i}}return t.list}})};d()}]),app.controller("editAddressController",["$scope","$http","$location","$routeParams","CONSTANT","apiConfig","feCache","feUI","feToast","feAreaPicker","feWebService","fePageService",function(e,t,n,a,i,o,r,s,c,l,u,d){var p=r.uc.get(i.CACHE.ADDRESS)||{},m=function(e){1==e&&d.setPageTitle("增添地址")},f=" ",g=a.type;m(g);var _,v=[p.province_name||"选择省",p.city_name||"选择市",p.district_name||(p.city_name?"":"选择区")];u.http(angular.extend({},o.api.province)).then(function(e){_=new l.AreaPicker({data:e,valSeparator:f,selectedData:[{areaname:v[0],areacode:p.province_code||null},{areaname:v[1],areacode:p.city_code||null},{areaname:v[2],areacode:p.district_code||null}],dataMap:{text:"areaname",value:"areacode",subs:"subarea"}})}),e.input={receiverName:new s.Input({lab:"收件人",val:p.recipient||"",placeholder:"姓名",inputClass:"black-light flex-1 ft-14",labelClass:"blue form-group-title",itemClass:"form-group flex ft-14",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"收件人不能为空"}}),receiverMobile:new s.InputMobile({lab:"联系电话",val:p.recipient_phone||"",placeholder:"收件人联系电话",inputClass:"black-light flex-1 ft-14",labelClass:"blue form-group-title",itemClass:"form-group flex ft-14"}),location:new s.Input({lab:"所在地区",val:v.join(" "),placeholder:"请选择所在地区",inputClass:"black-light flex-1 ft-14",labelClass:"blue form-group-title",itemClass:"form-group flex ft-14",readonly:!0,click:function(){_.show(e.input.location)}}),receiverAddress:new s.Input({lab:"详细街道",val:p.street_detail||"",placeholder:"街道门牌信息",inputClass:"black-light flex-1 ft-14",labelClass:"blue form-group-title",itemClass:"form-group flex ft-14 bb0",validation:{validator:function(e){return!!e},err:"输入街道信息有误"},maxlength:100})};var h=function(){var t=!0;for(var n in e.input)if(e.input.hasOwnProperty(n)&&(e.input[n].validate(),!e.input[n].isValid)){t=!1;break}return t},C=function(){var t=e.input.location.val;return!!t&&"选择省 选择市 选择区"!=t};e.button=new s.Button({text:"确定",click:function(){if(h())if(C()){var t=_.options.selectedData,a=t[0]||{},s=t[1]||{},l=t[2]||{},d={data:{recipient:e.input.receiverName.val,recipient_phone:e.input.receiverMobile.val,province_code:a.areacode,province_name:a.areaname,city_code:s.areacode,city_name:s.areaname,district_code:l.areacode,district_name:l.areaname,street_detail:e.input.receiverAddress.val}},m=!!r.uc.get(i.CACHE.ADDRESS);m&&(d.data=angular.extend(d.data,{address_id:p.address_id})),u.http(angular.extend(d,m?o.address.editAddress:o.address.addAddress)).then(function(){n.path("/address/manageAddress")})}else c.show("请选择所在地区")}})}]),app.controller("advertisement",["$scope","$location","$routeParams","feWebService","apiConfig","commonService","feUtils","feUI","feEnvService","envConfig","$http","feToast","commonConfig",function(e,t,n,a,i,o,r,s,c,l,u,d,p){function m(e){return/\d{1,5}/.test(e)}function f(){var t=!0;for(var n in e.input)if(e.input.hasOwnProperty(n)&&(e.input[n].validate(),!e.input[n].isValid)){t=!1;break}return t}function g(t){var n=e.channelId,a=["8","9","11","12"];if(t&&e.requestId&&a.indexOf(n)!==-1){var i=e.requestId,o=l.aiSubmitUrl+"?requestId="+i+"&status="+t+"&channel_id="+n+"&callback=JSON_CALLBACK";return void u.jsonp(o).success(function(e){})}}function _(){var t=e.input.receiverMobile.val;return r.isValidPhoneNumber(t)?(e.btn.otp.countdown(),void a.http(angular.extend({data:{phone_no:t,advert_id:e.advertId}},i.common.getOtp)).then(function(e){})):void d.show(p.msg.mobileNumError)}e.area=n.area||"440001",e.advertId=n.advert_id||"",e.channelId=n.channel_id||r.getParamFromURL("channel_id"),e.portal=n.portal||"",e.clickId=n.gdt_vid||r.getParamFromURL("gdt_vid"),e.requestId=n.requestId,e.url=t.absUrl(),e.show=!1,e.submitSuccess=!1,e.existsNum="10000",e.makeSure="",e.showRules=!0,"02"===e.channelId&&(e.clickId=n.qz_gdt||r.getParamFromURL("qz_gdt"));var v={1:"success",2:"error",3:"warning"};if(e.mod={show:function(t,n,a){e.submitSuccess=t,e.iconType="content-icon--"+v[n],e.submitTips=a,e.show=!0},close:function(){e.show=!1}},e.consumerHotline="4009998866",m(e.advertId)){var h={data:{advert_id:e.advertId}};a.http(angular.extend(h,i.advertisement.getExistsNum)).then(function(t){var n=t.data;n.hasOwnProperty("exists_num")&&(e.existsNum=n.exists_num)})}var C="";if(e.portal)C=c.getApiPrefix(),e.qrcodeHash=C+i.advertisement.qryChannelCode.url+"?portal="+e.portal;else{C=r.getCDNUrl()?r.getCDNUrl():"./";var b={440001:C+"asset/images/ad/ad_qrcode_GuangDong.png",320000:C+"asset/images/ad/ad_qrcode_JiangSu.png",440300:C+"asset/images/ad/ad_qrcode_ShenZhen.png",430000:C+"asset/images/ad/ad_qrcode_HuNan.jpg",610000:C+"asset/images/ad/ad_qrcode_ShanXi.png",330000:C+"asset/images/ad/ad_qrcode_ZheJiang.png"};e.qrcodeHash=b[e.area]}"440300"===e.area?e.isXWD=!0:e.isXWD=!1,e.input={enterprisName:new s.Input({lab:"",val:"",placeholder:"请输入企业名称",inputClass:"info-input",labelClass:"title",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"企业名称不能为空"}}),contactName:new s.Input({lab:"",val:"",placeholder:"请输入联系人",inputClass:"info-input",labelClass:"title title--diff",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"联系人不能为空"}}),receiverMobile:new s.InputMobile({lab:" ",val:"",placeholder:"请输入手机号码",inputClass:"info-input",labelClass:"title",itemClass:"consult-info__item"}),otp:new s.InputOTP({placeholder:"请输入验证码",inputClass:"info-input",labelClass:"title title--hide",itemClass:"consult-info__item",change:function(){this.validate()}})},g("0"),e.btn={otp:new s.ButtonOTP({click:function(){return!this.disabled()&&void _()}}),submit:function(){if(!e.advertId||!e.channelId)return e.mod.show(!1,"2","留资ID或渠道号获取不到！"),void(e.makeSure="返回");if(e.repeated=!0,f()){var t={data:{enterprise_name:e.input.enterprisName.val,contact_name:e.input.contactName.val,contact_phone_no:e.input.receiverMobile.val,channel_id:e.channelId,advert_id:e.advertId,click_id:e.clickId,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val}};a.http(angular.extend(t,i.advertisement.submitConsumerInfo)).then(function(t){0===t.status&&"SUCCESSED"===t.msg&&(e.mod.show(!0,"1","信息提交成功！"),e.makeSure="确定",e.repeated=!1)},function(t){e.mod.show(!1,"3",t.msg),e.makeSure="确定",e.repeated=!1}),g("1")}else e.mod.show(!1,"2","您输入的信息有误，请查正修改。"),e.makeSure="返回",e.repeated=!1}}}]),app.controller("brandName",["$scope","$location","$routeParams","feWebService","apiConfig","commonService","feUtils","feUI","feLoading","feEnvService","envConfig","$http","commonConfig","feCookie","feCache","feSession","CONSTANT","feToast","$timeout","$sce","weixinService","marketingService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h,C,b,y){var w=p.cache.cookie,S=m.get(w.advert_id.key),E=f.uc.get(_.CACHE.BRAND_NAME)||{},T={data:{advert_id:S}},A="asset"===n.page_type;a.http(angular.extend(T,i.advertisement.getExistsNum)).then(function(t){t&&(e.bransList=t.data.brandArry,e.filterBransList=e.bransList)});var N="asset/images/empty-tip.png";e.emptyPic=r.getCDNUrl()?r.getCDNUrl()+N:"./"+N,e.ord={obtian:function(e){m.set(w.brand_name.key,e.target.innerText);A?t.path("/leavePage/leaveStaticforDHD"):t.path("/leaveMessage"),E&&f.uc.extend(_.CACHE.ADVERTISEMENT,E)}},e.input={brands:new s.Input({lab:"",val:"",placeholder:"输入关键词查找",inputClass:"info-input",labelClass:"ui-icon-search",itemClass:"brands_input",change:function(){var t=e.input.brands.val;e.filterBransList=e.bransList.filter(function(e){return e.brand_name.indexOf(t)>-1})}})}}]),app.controller("leaveMessage",["$scope","$location","$routeParams","feWebService","apiConfig","commonService","feUtils","feUI","feLoading","feEnvService","envConfig","$http","commonConfig","feCookie","feCache","feSession","CONSTANT","feToast","$timeout","$sce","weixinService","marketingService","feAnalyticsService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h,C,b,y,w){function S(){var t=e.ordShow?e.input.receiverMobileOrd.val:e.input.receiverMobile.val;return r.isValidPhoneNumber(t)?(e.ordShow?e.btnOrd.otp.countdown():e.btn.otp.countdown(),void a.http(angular.extend({data:{phone_no:t,advert_id:e.advertId}},i.common.getOtp)).then(function(e){})):void v.show(p.msg.mobileNumError)}function E(e){return/\d{1,5}/.test(e)}function T(t,n){if(e.product_cd=t.product_cd,t){t.product_cd?"企业爱普app微信登录态下载模板"===t.template_name?(k("qyAppLoadShow"),$(t)):"有企业名称的app下载模板页面"===t.template_name?(k("qyAppHasNameShow"),M(t)):"订货贷"===t.product_cd?(e.ordShow=!0,e.entShow=!1,e.oldShow=!1,e.appLoadShow=!1,U(t),w.bpRouteChange("dhd_leaveMessage_enter")):"APP下载"===t.product_cd?(e.appLoadShow=!0,e.ordShow=!1,e.entShow=!1,e.oldShow=!1,R(t)):"供货贷"===t.product_cd?(k("ghdShow"),W(t)):"订货贷"!==t.product_cd&&(e.entShow=!0,e.ordShow=!1,e.oldShow=!1,e.appLoadShow=!1,D(t)):(a.http(angular.extend(n,i.advertisement.getExistsNum)).then(function(t){var n=t.data;n.hasOwnProperty("exists_num")&&(e.existsNum=n.exists_num)}),e.oldShow=!0,e.entShow=!1,e.ordShow=!1,O());var o=t.open_in_wechat;if("Y"===o){c.show(!0);var s=r.getParamFromURL("code");if(!s)return g.set(V,JSON.stringify(t)),void(window.location.href=b.getWeChatOAuthUrl(window.location.href));c.hide();var l=g.get(p.cache.session.ad_open_id.key),u=g.get(p.cache.session.ad_union_id.key);if(!u&&!l){var d={data:{code:s,client_sys_info:r.getClientSysInfo()}};y.commonLogin(d)}}}else a.http(angular.extend(n,i.advertisement.getExistsNum)).then(function(t){var n=t.data;n.hasOwnProperty("exists_num")&&(e.existsNum=n.exists_num)}),e.oldShow=!0,e.entShow=!1,e.ordShow=!1,O()}function A(){var t=!0;for(var n in e.input)if(e.input.hasOwnProperty(n)&&e.input[n].validate&&(e.input[n].validate(),!e.input[n].isValid)){t=!1;break}return t}function N(t){var n=e.channelId,a=["8","9","11","12"];if(t&&e.requestId&&a.indexOf(n)!==-1){var i=e.requestId,o=u.aiSubmitUrl+"?requestId="+i+"&status="+t+"&channel_id="+n+"&callback=JSON_CALLBACK";return void d.jsonp(o).success(function(e){})}}function I(){if("Y"===e.previewUrl){if("2"===e.nameCode)return e.modEnt.showEnt(!1,"2","预览界面不能提交留资信息！"),e.makeSure="返回",e.input.enterprisName.val="",e.input.contactName.val="",void(e.input.receiverMobile.val="");if("3"===e.nameCode)return e.modOrd.showOrd(!1,"2","预览界面不能提交留资信息！"),e.makeSure="返回",e.input.brandName.val="",e.input.enterprisNameOrd.val="",e.input.contactNameOrd.val="",e.input.receiverMobileOrd.val="",void(e.input.agentCode.val="");if("4"===e.nameCode)return e.modOrd.showOrd(!1,"2","预览界面不能提交留资信息！"),e.makeSure="返回",e.input.receiverMobile.val="",void(e.input.otp.val="");if(e.nameCode===F.HAS_ENTERPRISE_APP_DOWNLOAD)return e.modOrd.showOrd(!1,"2","预览界面不能提交留资信息！"),e.makeSure="返回",e.input.receiverMobile.val="",e.input.otp.val="",void(e.input.enterprisName.val="");if("6"===e.nameCode)return e.commonDialog.showCommonDialog(!1,"2","预览界面不能提交留资信息！"),e.makeSure="返回",void e.resetValues()}else{if(!e.advertId||!e.channelId)return"1"===e.nameCode?e.mod.show(!1,"2","留资ID或渠道号获取不到！"):"2"===e.nameCode?e.modEnt.showEnt(!1,"2","留资ID或渠道号获取不到！"):"3"===e.nameCode?e.modOrd.showOrd(!1,"2","留资ID或渠道号获取不到！"):"4"===e.nameCode?e.appLoad.showOrd(!1,"2","留资ID或渠道号获取不到！"):"6"===e.nameCode&&e.commonDialog.showCommonDialog(!1,"2","留资ID或渠道号获取不到！"),void(e.makeSure="返回");if(e.repeated=!0,A()){var t={};"3"===e.nameCode?(t={data:{brand_name:e.input.brandName.val,enterprise_name:e.input.enterprisNameOrd.val,contact_name:e.input.contactNameOrd.val,contact_phone_no:e.input.receiverMobileOrd.val,agent_code:e.input.agentCode.val,channel_id:e.channelId,advert_id:e.advertId,click_id:e.clickId,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val}},w.bpButtonClick("dhd_leaveMessage_submit")):"4"===e.nameCode?t={data:{contact_phone_no:e.input.receiverMobile.val,channel_id:e.channelId,advert_id:e.advertId,click_id:e.clickId,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val}}:e.nameCode===F.HAS_ENTERPRISE_APP_DOWNLOAD?(t={data:{enterprise_name:e.input.enterprisName.val,contact_phone_no:e.input.receiverMobile.val,channel_id:e.channelId,advert_id:e.advertId,click_id:e.clickId,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val}},q("buttonClick","has_enterprise_name_download",{})):t="6"===e.nameCode?{data:{enterprise_name:e.input.enterprisName.val,contact_name:e.input.contactName.val,contact_phone_no:e.input.receiverMobile.val,core_enterprise_name:e.input.coreEnterpriseName.val,channel_id:e.channelId,advert_id:e.advertId,click_id:e.clickId,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val}}:"2"!==e.nameCode||e.isAdvertShow?{data:{enterprise_name:e.input.enterprisName.val,contact_name:e.input.contactName.val,contact_phone_no:e.input.receiverMobile.val,channel_id:e.channelId,advert_id:e.advertId,click_id:e.clickId,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val}}:{data:{contact_phone_no:e.input.receiverMobile.val,channel_id:e.channelId,advert_id:e.advertId,click_id:e.clickId,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val}},t.data=Object.assign(r.getParamFromURL(),n,t.data),t.data.efs_channel_id=e.efs_channel_id,a.http(angular.extend(t,i.advertisement.submitConsumerInfo)).then(function(t){0===t.status&&"SUCCESSED"===t.msg&&("1"===e.nameCode?e.mod.show(!0,"1","信息提交成功！"):"2"===e.nameCode?(e.modEnt.showEnt(!0,"1","信息提交成功！"),e.imgShow=!0,e.input.enterprisName.val="",e.input.contactName.val="",e.input.receiverMobile.val=""):"3"===e.nameCode?(e.modOrd.showOrd(!0,"1","信息提交成功！"),e.imgShow=!0,e.input.brandName.val="",e.input.enterprisNameOrd.val="",e.input.contactNameOrd.val="",e.input.receiverMobileOrd.val="",e.input.agentCode.val=""):"4"===e.nameCode?(e.appLoad.showOrd(!0,"1","信息提交成功！"),e.imgShow=!0,e.input.receiverMobile.val="",e.input.otp.val="",x(e.button_ios,e.button_android)):e.nameCode===F.HAS_ENTERPRISE_APP_DOWNLOAD?(e.appLoad.showOrd(!0,"1","信息提交成功！"),e.imgShow=!0,e.input.receiverMobile.val="",e.input.otp.val="",e.input.enterprisName.val="",x(e.button_ios,e.button_android)):"6"===e.nameCode&&(e.commonDialog.showCommonDialog(!0,"1","信息提交成功！"),e.imgShow=!0,e.resetValues()),e.makeSure="确定",e.repeated=!1)},function(t){"1"===e.nameCode?e.mod.show(!1,"3",t.msg):"2"===e.nameCode?e.modEnt.showEnt(!1,"3",t.msg):"3"===e.nameCode?e.modOrd.showOrd(!1,"3",t.msg):"4"===e.nameCode?e.appLoad.showOrd(!1,"3",t.msg):"6"===e.nameCode?e.commonDialog.showCommonDialog(!1,"3",t.msg):e.nameCode===F.HAS_ENTERPRISE_APP_DOWNLOAD&&e.appLoad.showOrd(!1,"3",t.msg),e.makeSure="确定",e.repeated=!1}),N(1)}else"1"===e.nameCode?e.mod.show(!1,"2","您输入的信息有误，请查正修改。"):"2"===e.nameCode?e.modEnt.showEnt(!1,"2","您输入的信息有误，请查正修改。"):"3"===e.nameCode?e.modOrd.showOrd(!1,"2","您输入的信息有误，请查正修改。"):"4"===e.nameCode?e.appLoad.showOrd(!1,"2","您输入的信息有误，</br>请查证修改"):"6"===e.nameCode&&e.commonDialog.showCommonDialog(!1,"2","您输入的信息有误，</br>请查证修改"),e.makeSure="返回",e.repeated=!1}}function x(e,t){var n=Y?t:e;n||(n="https://sit.test.webankcdn.net/tcftp/cfa/cpmm.html"),window.location.href=n}function L(){document.body.scrollTop=0}function P(e,t){var n=e;if(!e&&t){var a="./";r.getCDNUrl()&&(a=r.getCDNUrl()),n=a+t}return n}function k(t){e.appLoadShow=!1,e.ordShow=!1,e.entShow=!1,e.oldShow=!1,e.qyAppLoadShow=!1,e[t]=!0}function O(){e.nameCode="1";var t="";if(e.portal)t=l.getApiPrefix(),e.qrcodeHash=t+i.advertisement.qryChannelCode.url+"?portal="+e.portal;else{t=r.getCDNUrl()?r.getCDNUrl():"./";var n={440001:t+"asset/images/ad/ad_qrcode_GuangDong.png",320000:t+"asset/images/ad/ad_qrcode_JiangSu.png",440300:t+"asset/images/ad/ad_qrcode_ShenZhen.png",430000:t+"asset/images/ad/ad_qrcode_HuNan.jpg",610000:t+"asset/images/ad/ad_qrcode_ShanXi.png",330000:t+"asset/images/ad/ad_qrcode_ZheJiang.png"};e.qrcodeHash=n[e.area]}"440300"===e.area?e.isXWD=!0:e.isXWD=!1,e.input={enterprisName:new s.Input({lab:"",val:"",placeholder:"请输入企业名称",inputClass:"info-input",labelClass:"title",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"企业名称不能为空"}}),contactName:new s.Input({lab:"",val:"",placeholder:"请输入联系人",inputClass:"info-input",labelClass:"title title--diff",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"联系人不能为空"}}),receiverMobile:new s.InputMobile({lab:" ",val:"",placeholder:"请输入手机号码",inputClass:"info-input",labelClass:"title",itemClass:"consult-info__item"}),otp:new s.InputOTP({placeholder:"请输入验证码",inputClass:"info-input",labelClass:"title title--hide",itemClass:"consult-info__item",change:function(){this.validate()}})},N(0),e.btn={otp:new s.ButtonOTP({click:function(){return!this.disabled()&&void S()}}),submit:function(){I()}}}function D(t){e.nameCode="2",t.page_name?document.title=t.page_name:document.title="微业贷";var n="";t.banner_one_url?e.bannerOneUrl=t.banner_one_url:(n=r.getCDNUrl()?r.getCDNUrl():"./",e.bannerOneUrl=n+"asset/images/ad/ent_banner.jpg"),t.banner_two_url&&"N"===t.show_banner_two?e.bannerTwoUrl=t.banner_two_url:t.banner_two_url&&"Y"===t.show_banner_two?e.bannerTwoUrl="":t.banner_two_url||"Y"!==t.show_banner_two?(n=r.getCDNUrl()?r.getCDNUrl():"./",e.bannerTwoUrl=n+"asset/images/ad/banner2.png"):e.bannerTwoUrl="",t.banner_three_url&&"N"===t.show_banner_three?e.bannerThreeUrl=t.banner_three_url:t.banner_three_url&&"Y"===t.show_banner_three?e.bannerThreeUrl="":t.banner_three_url||"Y"!==t.show_banner_three?(n=r.getCDNUrl()?r.getCDNUrl():"./",e.bannerThreeUrl=n+"asset/images/ad/banner_three _ent .png"):e.bannerThreeUrl="",t.banner_four_url&&"N"===t.show_banner_four?e.bannerFourUrl=t.banner_four_url:t.banner_four_url&&"Y"===t.show_banner_four?e.bannerFourUrl="":t.banner_four_url||"Y"!==t.show_banner_four?(n=r.getCDNUrl()?r.getCDNUrl():"./",e.bannerFourUrl=n+"asset/images/ad/banner_four _ent .png"):e.bannerFourUrl="",t.model_two_title?e.modelTwoTitle=t.model_two_title:e.modelTwoTitle="产品申请咨询",t.button_one_text?e.buttonOneText=t.button_one_text:e.buttonOneText="立刻申请",t.button_one_color?e.buttonOneColor=t.button_one_color:e.buttonOneColor="#4386ff",t.text_one?e.textOne=t.text_one:e.isTextOneShow=!0,t.text_two?e.textTwo=t.text_two:e.isTextTwoShow=!0,t.text_three?e.textThree=t.text_three:e.isTextThreeShow=!0,e.input={receiverMobile:new s.InputMobile({lab:" ",val:"",placeholder:"请输入",containerClass:"outerLayer",inputClass:"box__input",labelClass:"title",itemClass:"consult-info__item",blur:function(){L()}}),enterprisName:e.isAdvertShow?new s.Input({lab:"",val:"",placeholder:"请输入",containerClass:"outerLayer",inputClass:"box__input",labelClass:"title",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"企业名称不能为空"},change:function(){var t=e.input.enterprisName.val.length;if(G&&h.cancel(G),e.input.enterprisName.val||(e.isCompanyShow=!1),t>0){var n=[],o={data:{root:e.input.enterprisName.val}};e.input.receiverMobile.val&&r.isValidPhoneNumber(e.input.receiverMobile.val)&&(G=h(function(){a.http(angular.extend(o,i.advertisement.getAssociatedNames)).then(function(t){n=t.data.list,n.length>0&&(e.selectList=n,e.isCompanyShow=!0)})},400)),e.onItemDown=function(e){e.preventDefault()},e.onItemClick=function(t){e.isCompanyShow=!1,e.input.enterprisName.val=n[t]}}else e.isCompanyShow=!1},blur:function(){e.isCompanyShow=!1,L()}}):{},contactName:e.isAdvertShow?new s.Input({lab:"",val:"",placeholder:"请输入",containerClass:"outerLayer",inputClass:"box__input",labelClass:"title title--diff",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"联系人不能为空"},blur:function(){L()}}):{},otp:new s.InputOTP({placeholder:"请输入验证码",inputClass:"info-input",labelClass:"title title--hide",itemClass:"consult-info__item",change:function(){this.validate()},blur:function(){L()}})},N(0),e.btn={otp:new s.ButtonOTP({click:function(){return!this.disabled()&&void S()}}),submitEnt:function(){I()}}}function U(n){e.nameCode="3",n.page_name?document.title=n.page_name:document.title="订货贷";var o="";n.banner_one_url?e.bannerOneUrlOrd=n.banner_one_url:(o=r.getCDNUrl()?r.getCDNUrl():"./",e.bannerOneUrlOrd=o+"asset/images/ad/ord_banner.png");var c="asset/images/ad/serive_icon.png";e.seriveTelOrd=r.getCDNUrl()?r.getCDNUrl()+c:"./"+c,n.icon_one_url?e.iconOneUrl=n.icon_one_url:(o=r.getCDNUrl()?r.getCDNUrl():"./",e.iconOneUrl=o+"asset/images/ad/icon_left.png"),n.icon_two_url?e.iconTwoUrl=n.icon_two_url:(o=r.getCDNUrl()?r.getCDNUrl():"./",e.iconTwoUrl=o+"asset/images/ad/icon_right.png"),n.icon_text_one?e.iconTextOne=n.icon_text_one:e.iconTextOne="成立满半年公司或个体户",n.icon_text_two?e.iconTextTwo=n.icon_text_two:e.iconTextTwo="与品牌合作订货业务",n.text_one?e.textOne=n.text_one:e.isTextOneShow=!0,e.consumerHotline=n.text_one?n.text_one:"4009998866",n.text_two?e.textTwo=n.text_two:e.isTextTwoShow=!0,n.text_three?e.textThree=n.text_three:e.isTextThreeShow=!0;var l=p.cache.cookie;e.ord={choice:function(){m.set(l.advert_id.key,e.advertId);f.uc.extend(_.CACHE.BRAND_NAME,{enterprise_name:e.input.enterprisNameOrd.val,contact_name:e.input.contactNameOrd.val,contact_phone_no:e.input.receiverMobileOrd.val,agent_code:e.input.agentCode.val}),t.path("/advertisement/brandName")}};var u=m.get(l.brand_name.key);e.input={brandName:new s.Input({lab:"",val:u,placeholder:"请选择",containerClass:"outerLayerOrd",warningClass:"opt-warning",inputClass:"box__input",labelClass:"title",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"所属品牌不能为空"},blur:function(){L()}}),receiverMobileOrd:new s.InputMobile({lab:" ",val:"",placeholder:"请输入",containerClass:"outerLayer",warningClass:"opt-warning",inputClass:"box__input",labelClass:"title",itemClass:"consult-info__item",blur:function(){L()}}),enterprisNameOrd:new s.Input({lab:"",val:"",placeholder:"请输入营业执照上企业全称",containerClass:"outerLayer",inputClass:"info-enter box__input",warningClass:"opt-warning",labelClass:"title",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return void 0!=e&&""!=e&&""!=e.trim()},err:"企业名称不能为空"},change:function(){var t=e.input.enterprisNameOrd.val.length;if(G&&h.cancel(G),e.input.enterprisNameOrd.val||(e.isCompanyShow=!1),t>0){var n=[],o={data:{root:e.input.enterprisNameOrd.val}};e.input.receiverMobileOrd.val&&r.isValidPhoneNumber(e.input.receiverMobileOrd.val)&&(G=h(function(){a.http(angular.extend(o,i.advertisement.getAssociatedNames)).then(function(t){n=t.data.list,n.length>0&&(e.selectList=n,e.isCompanyShow=!0)})},400)),e.onItemDown=function(e){event.preventDefault()},e.onItemClick=function(t){e.isCompanyShow=!1,e.input.enterprisNameOrd.val=n[t]}}else e.isCompanyShow=!1},blur:function(){e.isCompanyShow=!1,L()}}),contactNameOrd:new s.Input({lab:"",val:"",placeholder:"请填写",containerClass:"outerLayer",warningClass:"opt-warning",inputClass:"box__input",labelClass:"title title--diff",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return void 0!=e&&""!=e&&""!=e.trim();
// },err:"联系人不能为空"},blur:function(){L()}}),agentCode:new s.Input({lab:"",val:"",placeholder:"选填",containerClass:"outerLayer",inputClass:"box__input",labelClass:"title title--diff",itemClass:"consult-info__item",warningClass:"opt-warning",maxlength:50,blur:function(){L()}}),otp:new s.InputOTP({lab:" ",placeholder:"请输入验证码",containerClass:"outerLayer",inputClass:"box__input",labelClass:"title title--hide",itemClass:"consult-info__item",warningClass:"opt-warning",change:function(){this.validate()},blur:function(){L()}})};var d=f.uc.get(_.CACHE.ADVERTISEMENT)||{};d&&(e.input.enterprisNameOrd.val=d.enterprise_name,e.input.contactNameOrd.val=d.contact_name,e.input.receiverMobileOrd.val=d.contact_phone_no,e.input.agentCode.val=d.agent_code),N(0),e.btnOrd={otp:new s.ButtonOTP({click:function(){return!this.disabled()&&void S()}}),submitOrd:function(){I()}}}function M(t){r.setNonWritableKey(e,"nameCode",F.HAS_ENTERPRISE_APP_DOWNLOAD),R(t),e.input=Object.assign({enterprisName:new s.Input({lab:"",val:"",placeholder:"请输入企业名称",inputClass:"info-input",hideLabel:!0,labelClass:"label-input",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"企业名称不能为空"}})},e.input),t.banner_three_url&&(e.bannerThreeUrl=t.banner_three_url),t.banner_four_url&&(e.bannerFourUrl=t.banner_four_url)}function R(t){e.nameCode="4",document.title=t.page_name||"APP下载";var n="";t.banner_one_url?e.bannerOneUrl=t.banner_one_url:(n=r.getCDNUrl()?r.getCDNUrl():"./",e.bannerOneUrl=n+"asset/images/ad/app-download-banner.jpg"),t.banner_two_url?e.bannerTwoUrl=t.banner_two_url:(n=r.getCDNUrl()?r.getCDNUrl():"./",e.bannerTwoUrl=n+"asset/images/ad/app-download-banner2.jpg"),e.buttonOneText=t.button_one_text||"提交",e.buttonOneColor=t.button_one_color||"#4386ff",e.button_ios=t.button_ios,e.button_android=t.button_android,e.input={receiverMobile:new s.InputMobile({lab:" ",val:"",placeholder:"请输入您的手机号",inputClass:"info-input",hideLabel:!0,labelClass:"label-input",blur:function(){L()}}),otp:new s.Input({placeholder:"请输入验证码",inputClass:"otp-input",maxlength:6,val:"",labelClass:"label-input",validation:{validator:r.isValidPhoneOtpNumber,err:"输入验证码有误"},change:function(){this.validate()},blur:function(){L()}})},N(0),e.btn={otp:new s.ButtonOTP({click:function(){return!this.disabled()&&void S()}}),submitOrd:function(){I()}}}function $(t){var n=JSON.parse(g.get("register_info")||"{}").value||{},a=n.phone_number||"";q("buttonClick","qyapp_leaveMessage_enter",{phone_num:a});var i="asset/images/ad/";e.nameCode="5",document.title=t.page_name||"企业爱普APP下载",e.bannerOneUrl=P(t.banner_one_url,i+"qyapp_banner_1.jpg"),e.bannerTwoUrl=P(t.banner_two_url,i+"qyapp_banner_2.jpg"),e.bannerThreeUrl=P(t.banner_three_url,i+"qyapp_banner_3.jpg"),e.bannerFourUrl=P(t.banner_four_url,i+"qyapp_banner_4.png"),e.buttonOneText=t.button_one_text||"立即下载",e.buttonOneColor=t.button_one_color||"#ff8543",e.download=function(){function e(e){c=new CallApp({scheme:{protocol:"webank-cfa",host:"main"},intent:{package:"com.webank.cfa",scheme:"webank-cfa"},appstore:r,yingyongbao:s,fallback:o?e:r,timeout:3e3})}function t(){c?c.open({path:""}):window.location.href=r}q("buttonClick","ad_enterprise_app_download",{phone_num:a});var n=navigator.userAgent.match(/micromessenger\/([\d.]+)/i);if(n){var i="https://webankcdn.net/tcftp/cfa/cpmm.html";window.location.href=i}var o=navigator.userAgent.match(/Android/i),r=(navigator.userAgent.match(/iPhone|iPad|iPod/i),"https://apps.apple.com/cn/app/%E5%BE%AE%E4%BC%97%E4%BC%81%E4%B8%9A%E7%88%B1%E6%99%AE/id1455485724"),s="https://sj.qq.com/myapp/detail.htm?apkName=com.webank.cfa",c=null;o?(e("https://webankcdn.net/tcftp/cfa/cpmm.html"),t()):t()},N(0)}function B(e){return e||0===e}function H(){"undefined"!=typeof wa&&(wa.setParam("field_y_3",e.channelId),wa.setParam("field_y_4",e.advertId),wa.setParam("field_y_5",e.efs_channel_id))}function q(t,n,a){if("undefined"!=typeof wa&&n&&B(t)){var i=g.get(p.cache.session.ad_open_id.key),o=g.get(p.cache.session.ad_union_id.key);wa.setParam("field_y_0",o),wa.setParam("openId",i),wa.setParam("field_y_3",e.channelId),wa.setParam("field_y_4",e.advertId),wa.setParam("field_y_5",e.efs_channel_id),wa.clickStat(n,t,angular.extend({channel_id:e.channelId,advert_id:e.advertId,efs_channel_id:e.efs_channel_id,open_id:i,union_id:o},a))}}function W(t){e.nameCode="6",document.title=t.page_name||"供货贷",N(0),e.btn={otp:new s.ButtonOTP({click:function(){return!this.disabled()&&void S()}}),submitOrd:function(){I()},submitBtnDisabled:!0,checkDisabled:function(){var t=e.input;e.btn.submitBtnDisabled=!(t.enterprisName.val&&t.contactName.val&&r.isValidPhoneNumber(t.receiverMobile.val)&&t.coreEnterpriseName.val&&r.isValidPhoneOtpNumber(t.otp.val))}},e.input={isCoreEnterpriseName:!1,coreEnterpriseSelectList:[],enterprisName:new s.Input({lab:"",val:"",placeholder:"请输入企业名称",containerClass:"outerLayer",inputClass:"info-enter",labelClass:"title",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return void 0!==e&&""!==e.trim()},err:"企业名称不能为空"},change:function(){var t=e.input.enterprisName.val.length;if(G&&h.cancel(G),e.input.enterprisName.val||(e.isCompanyShow=!1),t>0){var n=[],o={data:{root:e.input.enterprisName.val}};G=h(function(){a.http(angular.extend(o,i.advertisement.getAssociatedNames)).then(function(t){n=t.data.list,n.length>0&&(e.selectList=n,e.isCompanyShow=!0)})},400),e.onItemDown=function(e){event.preventDefault()},e.onItemClick=function(t){e.isCompanyShow=!1,e.input.enterprisName.val=n[t]}}else e.isCompanyShow=!1;e.btn.checkDisabled()},blur:function(){e.isCompanyShow=!1,L()}}),contactName:new s.Input({lab:"",val:"",placeholder:"请输入法人代表人姓名",containerClass:"outerLayer",inputClass:"box__input",labelClass:"title title--diff",itemClass:"consult-info__item",maxlength:50,validation:{validator:function(e){return void 0!=e&&""!=e&&""!=e.trim()},err:"联系人不能为空"},blur:function(){L()},change:function(){e.btn.checkDisabled()}}),receiverMobile:new s.InputMobile({lab:" ",val:"",placeholder:"请输入法人代表人手机号",containerClass:"outerLayer",inputClass:"box__input",labelClass:"title",itemClass:"consult-info__item",blur:function(){L()},change:function(){e.btn.checkDisabled()}}),coreEnterpriseName:{val:"",blur:function(){e.input.isCoreEnterpriseName=!1,L()},change:function(){var t=e.input.coreEnterpriseName.val.length;if(G&&h.cancel(G),e.input.coreEnterpriseName.val||(e.input.isCoreEnterpriseName=!1),t>0){var n=[],o={data:{root:e.input.coreEnterpriseName.val}};G=h(function(){a.http(angular.extend(o,i.advertisement.getAssociatedNames)).then(function(t){n=t.data.list,n.length>0&&(e.input.coreEnterpriseSelectList=n,e.input.isCoreEnterpriseName=!0)})},400),e.input.onItemDown=function(e){event.preventDefault()},e.input.onItemClick=function(t){e.input.isCoreEnterpriseName=!1,e.input.coreEnterpriseName.val=n[t]}}else e.input.isCoreEnterpriseName=!1;e.btn.checkDisabled()}},otp:new s.InputOTP({placeholder:"请输入验证码",inputClass:"info-input",labelClass:"otp-title",itemClass:"consult-info__item",change:function(){this.validate(),e.btn.checkDisabled()},blur:function(){L()}})},e.resetValues=function(){e.input.enterprisName.val="",e.input.contactName.val="",e.input.receiverMobile.val="",e.input.coreEnterpriseName.val="",e.input.otp.val="",e.btn.otp.reset(),e.btn.checkDisabled()}}var Y=navigator.userAgent.match(/Android/i),V="AD_PAGE_DATA",F={HAS_ENTERPRISE_APP_DOWNLOAD:"HAS_ENTERPRISE_APP_DOWNLOAD"};e.area=n.area||"440001",e.advertId=n.advert_id||"1111",e.channelId=n.channel_id||r.getParamFromURL("channel_id")||"22222",e.efs_channel_id=n.efs_channel_id||r.getParamFromURL("efs_channel_id"),e.portal=n.portal||"",e.previewUrl=n.previewUrl||"",e.clickId=n.gdt_vid||r.getParamFromURL("gdt_vid"),e.requestId=n.requestId,e.url=t.absUrl(),e.ghdShow=!1,e.entShow=!1,e.ordShow=!1,e.oldShow=!1,e.appLoadShow=!1,e.qyAppLoadShow=!1,e.show=!1,e.showOrd=!1,e.showEnt=!1,e.appLoadDialogShow=!1,e.submitSuccess=!1,e.isSubmitSuccess=!1,e.existsNum="10000",e.imgShow=!1,e.makeSure="",e.showRules=!0,e.modelTwoTitle="",e.buttonOneText="",e.buttonOneColor="",e.bannerOneUrl="",e.bannerTwoUrl="",e.bannerThreeUrl="",e.bannerFourUrl="",e.textOne="",e.textTwo="",e.textThree="",e.isTextOneShow=!1,e.isTextTwoShow=!1,e.isTextThreeShow=!1,e.iconTextOne="",e.iconTextTwo="",e.bannerOneUrlOrd="",e.iconOneUrl="",e.iconTwoUrl="",e.seriveTelOrd="",e.isCompanyShow=!1,e.nameCode="",e.commonDialogShow=!1,e.consumerHotline="4009998866",e.isAdvertShow="00154"!==e.advertId;var G=void 0;e.iconLeftImg=r.getCDNUrl()?r.getCDNUrl()+"asset/images/ad/ent_title_left.png":"./asset/images/ad/ent_title_left.png",e.iconRightImg=r.getCDNUrl()?r.getCDNUrl()+"asset/images/ad/ent_title_right.png":"./asset/images/ad/ent_title_right.png",e.iconArrowImg=r.getCDNUrl()?r.getCDNUrl()+"asset/images/ad/depres.png":"./asset/images/ad/depres.png","02"===e.channelId?e.clickId=n.qz_gdt||r.getParamFromURL("qz_gdt"):"03"===e.channelId&&(e.clickId=n.clickid||r.getParamFromURL("clickid"));var z={1:"success",2:"error",3:"warning"};if(e.mod={show:function(t,n,a){e.submitSuccess=t,e.iconType="content-icon--"+z[n],e.submitTips=a,e.show=!0},close:function(){e.show=!1}},e.modEnt={showEnt:function(t,n,a){e.submitSuccess=t,e.isSubmitSuccess=!t,e.iconType="content-icon--"+z[n],e.submitTips=a,e.showEnt=!0},closeEnt:function(){e.showEnt=!1}},e.modOrd={showOrd:function(t,n,a){e.submitSuccess=t,e.isSubmitSuccess=!t,e.iconType="content-icon--"+z[n],e.submitTips=a,e.showOrd=!0},closeOrd:function(){e.imgShow=!1,e.showOrd=!1}},e.appLoad={showOrd:function(t,n,a){e.submitSuccess=t,e.isSubmitSuccess=!t,e.iconType="content-icon--"+z[n],e.submitTips=C.trustAsHtml(a),e.appLoadDialogShow=!0},closeOrd:function(){e.appLoadDialogShow=!1}},e.commonDialog={showCommonDialog:function(t,n,a){e.submitSuccess=t,e.isSubmitSuccess=!t,e.iconType="content-icon--"+z[n],e.submitTips=C.trustAsHtml(a),e.commonDialogShow=!0},closeCommonDialog:function(){e.commonDialogShow=!1}},H(),E(e.advertId)){var j=g.get(V);j=j?JSON.parse(j):j;var J={data:{advert_id:e.advertId,previewUrl:e.previewUrl}};j?T(j,J):a.http(angular.extend(J,i.advertisement.qryAdvertPageSet)).then(function(e){var t=e.data;T(t,J)})}}]),app.controller("allowListQueryController",["$scope","$location","CONSTANT","apiConfig","$routeParams","PageList","feCache","feUI","feConfirm","feWebService","exchangeDetailService","feUtils","feToast","commonConfig","commonService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f){function g(){document.body.scrollTop=0}function _(){return Object.values(e.inputs).every(function(e){return e.validate(),e.isValid})}function v(){function t(){if(_()){var t=e.phoneNo,n=i.channel_id,o=e.inputs.enterprisName.val,r=e.inputs.code.val;l.http(angular.extend({data:{phone_no:t,channel_id:n,enterprise_name:o,social_unity_credit_code:r}},a.allowListQuery.queryBusinessInfo)).then(function(e){e.data&&e.data.exist?b.show(e.data.enterprise_name+"，查询通过","确定"):b.show((o||r)+"，查询不通过","确定")},function(e){23414020==e.status&&h(),b.show(e.msg,"我知道了")})}}function n(){e.btns.qrySubmit.disabled=!Object.keys(e.inputs).some(function(t){var n=e.inputs[t].val.trim();return n})}e.showQry=!0,e.showOtp=!1,e.inputs={enterprisName:new s.Input({lab:"企业名称",val:"",placeholder:"请输入企业名称",containerClass:"inquiry-group",inputClass:"group-input",warningClass:"opt-warning allow-page-warning",labelClass:"group-label",itemClass:"consult-info__item inquiry-item",maxlength:20,change:function(){n()}}),code:new s.Input({lab:"统一社会信用代码",val:"",placeholder:"请输入统一社会信用代码",containerClass:"inquiry-group",inputClass:"group-input",warningClass:"opt-warning allow-page-warning",labelClass:"group-label",itemClass:"consult-info__item inquiry-item",maxlength:18,validation:{validator:function(e){var t=/^[a-zA-Z\d]+$/;return!(""!=e&&""!=e.trim()&&!t.test(e))},err:"请输入正确的统一社会信用代码"},change:function(){n()}})},e.btns={qrySubmit:new s.Button({text:"提交",disabled:!0,btnClass:"btn-normal",click:function(){t()}})}}function h(){function t(){var t=e.inputs.phone.val;return d.isValidPhoneNumber(t)?(e.btn.otp.countdown(),void l.http(angular.extend({data:{phone_no:t}},a.allowListQuery.getOtp)).then(function(e){})):void p.show(m.msg.mobileNumError)}function n(){if(_()){var t=e.inputs.phone.val,n=e.inputs.otp.val,o=i.channel_id;l.http(angular.extend({data:{phone_no:t,otp_code:n,channel_id:o}},a.allowListQuery.login)).then(function(n){e.phoneNo=t,v()},function(t){23414020==t.status?e.inputs.otp.hide=!1:b.show(t.msg,"我知道了")})}}e.showQry=!1,e.showOtp=!0,e.inputs={phone:new s.InputMobile({lab:"手机号码",val:"",placeholder:"请输入您的手机号",containerClass:"inquiry-group",inputClass:"group-input",warningClass:"opt-warning allow-page-warning",labelClass:"group-label",itemClass:"consult-info__item",blur:function(){g()}}),otp:new s.InputOTP({placeholder:"请输入验证码",hide:!0,containerClass:"inquiry-group",inputClass:"group-input",warningClass:"opt-warning allow-page-warning",labelClass:"group-label",itemClass:"consult-info__item",change:function(){this.validate()},blur:function(){g()}})},e.btn={otp:new s.ButtonOTP({btnClass:"group-div",click:function(){return!this.disabled()&&void t()}}),otpSubmit:new s.Button({text:"查询",btnClass:"btn-normal",click:function(){n()}})}}function C(){h()}e.showQry=!1,e.showOtp=!1,e.phoneNo="",i.channel_id||f.goErrorMsg({},!0),e.diolog={show:!1,btnText:"我知道了",content:"查询功能失效，不可查询!"};var b={show:function(t,n){e.diolog={show:!0,btnText:n,content:t}},close:function(){e.diolog={show:!1,btnText:"",content:""}}};e.closeDiolog=b.close,C()}]),app.controller("msgController",["$scope","$sce","$routeParams","feUI","feCache","feCookie","CONSTANT","commonConfig","feToast","weixinService",function(e,t,n,a,i,o,r,s,c,l){var u={400:"安全认证失败",501:"非法的请求","50x":"系统异常，请联系客服"},d=i.uc.get(r.CACHE.COMMON_MSG)||{title:"出错了",content:s.msg.sysBusyError},p=n.errorType?u[n.errorType]:"";p&&(d.content=p),e.showCloseBtn=!1,e.title=d.title,e.content=t.trustAsHtml(d.content),d.showCloseBtn&&(e.showCloseBtn=!0,e.closeBtn=new a.Button({text:"确定",click:function(){l.invoke("closeWindow")}}))}]),app.controller("couponActivityController",["$scope","$location","$routeParams","CONSTANT","feWebService","feUI","apiConfig","commonService","feCache","couponService","$sce",function(e,t,n,a,i,o,r,s,c,l,u){var d=n.activity_id;if(!d)return void s.goErrorMsg({},!0);if(e.activity=c.uc.get(a.CACHE.COUPON_ACTIVITY),e.activity)e.activity.activity_desc=u.trustAsHtml(e.activity.activity_desc.replace(/\n/g,"<br>"));else{var p={data:{activity_id:d}};i.http(angular.extend(p,r.coupon.getActivityInfo)).then(function(t){t&&t.data?(e.activity=t.data,e.activity.activity_desc=u.trustAsHtml(e.activity.activity_desc.replace(/\n/g,"<br>"))):s.goErrorMsg({},!0)})}e.btn={joinBtn:new o.Button({text:"立即参加",waValue:"join_Btn",click:function(){t.path("/coupon/myCoupon").search({activity_id:d})}}),recommendBtn:new o.Button({text:"推荐给好友",waValue:"recommend_Btn",click:function(){l.showCouponActivityShareLayer({activityId:d})}})}}]),app.controller("couponActivityDetailsController",["$scope","$sce","feCache","CONSTANT","$routeParams","commonService","apiConfig","feWebService",function(e,t,n,a,i,o,r,s){var c=n.uc.get(a.CACHE.COUPON_ACTIVITY),l=i.activity_id;if(c)e.data={activityDecr:c.activity_desc,activityRules:t.trustAsHtml(c.activity_rule_desc.replace(/\n/g,"<br>"))};else{if(!l)return void o.goErrorMsg({},!0);var u={data:{activity_id:l}};s.http(angular.extend(u,r.coupon.getActivityInfo)).then(function(i){i&&i.data?(n.uc.put(a.CACHE.COUPON_ACTIVITY,i.data),e.data={activityDecr:i.data.activity_desc,activityRules:t.trustAsHtml(i.data.activity_rule_desc.replace(/\n/g,"<br>"))}):o.goErrorMsg({},!0)},function(){o.goErrorMsg({msg:"暂时无法获取活动信息"},!0)})}}]),app.controller("followOfficialAccountsController",["$scope","feUtils","feEnvService","publicConfig",function(e,t,n,a){var i="";i=t.getCDNUrl()&&"undefined/"!==t.getCDNUrl()?t.getCDNUrl():"./",e.qrcodeImg=i+a.qrcodeURL,e.WeChatSubsription=a.WeChatSubsription}]);var _slicedToArray=function(){function e(e,t){var n=[],a=!0,i=!1,o=void 0;try{for(var r,s=e[Symbol.iterator]();!(a=(r=s.next()).done)&&(n.push(r.value),!t||n.length!==t);a=!0);}catch(e){i=!0,o=e}finally{try{!a&&s.return&&s.return()}finally{if(i)throw o}}return n}return function(t,n){if(Array.isArray(t))return t;if(Symbol.iterator in Object(t))return e(t,n);throw new TypeError("Invalid attempt to destructure non-iterable instance")}}();app.controller("myCouponController",["$scope","$location","$routeParams","CONSTANT","commonConfig","feWebService","feUI","apiConfig","commonService","couponService","feCache","feToast","feAlert",function(e,t,n,a,i,o,r,s,c,l,u,d,p){var m=n.activity_id;if(!m)return void c.goErrorMsg({},!0);var f=i.coupon.slogen;e.stepstatus={status:["领取成功","绑定成功","使用成功"],current:0},e.actions={btnText:"",btnWaValue:"",disabled:!1,status:0,slogen:""};var g={data:{activity_id:m}},_=function(){o.http(angular.extend(g,s.coupon.getActivityInfo)).then(function(t){t&&t.data?(e.couponId=t.data.free_coupons_id,u.uc.put(a.CACHE.COUPON_ACTIVITY,t.data),e.activity={activityName:t.data.activity_name,start:"Y"==t.data.is_start,end:!("N"==t.data.is_end||!t.data.is_end),current:0,couponName:t.data.free_coupons_name,faceValue:t.data.face_value,maxLoanAmt:t.data.max_loan_amt,effectivedays:t.data.effective_days,activityDate:t.data.start_time.slice(0,10)+"~"+t.data.end_time.slice(0,10),activity_desc:t.data.activity_desc,is_receive:"Y"==t.data.is_receive,is_quota:"Y"==t.data.is_quota,is_used:"Y"==t.data.is_used},v({is_receive:e.activity.is_receive,is_quota:e.activity.is_quota,is_used:e.activity.is_used}),h(e.activity.end,e.activity.is_receive,e.activity.is_quota,e.activity.is_used),e.couponBtn=new r.Button({text:e.actions.btnText,waValue:e.actions.btnWaValue,disabled:e.actions.disabled,click:function(){var t=this,n=function(){return new Map([[{status:0},function(){C()}],[{status:2},function(){l.goConponList({free_coupons_type:"01"})}],[{status:3},function(){l.goConponList({free_coupons_type:"02"})}],[{status:4},function(){l.goConponList({free_coupons_type:"02"})}]])},a=[].concat(_toConsumableArray(n())).filter(function(t){var n=_slicedToArray(t,2),a=n[0];n[1];return a.status==e.actions.status});a.forEach(function(e){var n=_slicedToArray(e,2),a=(n[0],n[1]);return a.call(t)})}})):c.goErrorMsg({},!0)},function(){c.goErrorMsg({msg:"暂时无法获取活动信息"},!0)})};_(),e.evt={checkDetails:function(){t.path("/coupon/couponActivityDetails")},following:function(){t.path("/coupon/followOfficialAccounts")}};var v=function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},n=t.is_receive||!1,a=t.is_quota||!1,i=t.is_used||!1;n&&(e.stepstatus.current=1),n&&a&&(e.stepstatus.current=2),i&&(e.stepstatus.current=3)},h=function(t,n,a,i){var o=function(t,n,a,i){e.actions={btnText:t,btnWaValue:n,disabled:a,status:i||0,slogen:f[i].text}};return t||n?t&&!n?void o("已结束","is_end_btn",!0,1):n&&!a?void o("去绑定","apply_btn",!1,2):i?void(i&&o("已使用","used_btn",!0,5)):t?void o("去使用","apply_btn",!1,4):void o("去使用","apply_btn",!1,3):void o("领取","coupon_receive_btn",!1,0)},C=function(){return e.activity.start?void o.http(angular.extend(g,s.coupon.seize)).then(function(t){if(t&&t.data){var n=t.data.result;switch(n){case"0":var i=u.uc.get(a.CACHE.COUPON_ACTIVITY);u.uc.put(a.CACHE.COUPON_ACTIVITY,angular.extend(i,t.data)),b({title:"恭喜老板",content:"领取成功",result:n});break;case"1":b({title:"来晚了",content:"本期免息券已被抢光，敬请期待下期活动！",result:n}),e.actions.slogen=f[6].text;break;case"2":e.activity.end=!0;break;case"3":d.showMask("您已参与过本活动",3e3);break;case"99":d.showMask("当前参与活动的人太多，请退出后重新进入领取页面",3e3);break;case"4":b({title:"领取失败",content:"抱歉！您暂不符合本期活动规则！",result:n});break;default:c.goErrorMsg({},!0)}}else c.goErrorMsg({},!0)}):void b({title:"领取失败",content:"活动未开始"})},b=function(e){var t=e.title||"",n=e.content||"",a=e.btnText||"朕知道了",i=e.result||0;p.show({title:t,content:n,btn:{text:a,click:function(){"0"==i&&_(),document.querySelectorAll(".mod-mask")[0].classList.remove("coupon-mod")}}}),document.querySelectorAll(".mod-mask")[0].classList.add("coupon-mod")}}]),app.controller("emptyCouponController",["$scope","$routeParams","commonService",function(e,t,n){var a={"01":"活动太火热<br>免息券被抢完"},i=t.type;i&&a.hasOwnProperty(i)?e.msg=a[i]:n.goErrorMsg({},!0)}]),app.controller("getShareCouponController",["$scope","$location","$routeParams","CONSTANT","apiConfig","feCache","feWebService","commonService","couponService","feToast","publicConfig",function(e,t,n,a,i,o,r,s,c,l,u){e.couponId=n.free_coupons_id,e.productName=u.productName;var d=n.transfer_id;if(!e.couponId||!d)return void s.goErrorMsg({},!0);if(e.activity=o.uc.get(a.CACHE.COUPON_ACTIVITY),!e.activity){var p={data:{free_coupons_id:e.couponId,transfer_id:d}};r.http(angular.extend(p,i.coupon.queryCoupon)).then(function(t){t&&t.data&&(e.activity=t.data)})}e.btn={share:function(){var t=e.couponId;c.showCouponShareLayer({couponId:t})},getCoupon:function(){var n={data:{transfer_id:d,free_coupons_id:e.couponId}};r.http(angular.extend(n,i.coupon.getCoupon)).then(function(e){if(e&&e.data){var n=e.data.result;"0"==n||"1"==n?t.path("/coupon/share").search({free_coupons_id:e.data.free_coupons_id,transfer_id:d,force:!0}):"2"==n||"3"==n?t.path("/coupon/notice").search({type:"01",is_self:"N"}):"99"==n?l.showMask("当前参与活动的人太多，请退出后重新进入领取页面",3e3):s.goErrorMsg({},!0)}})}}}]),app.controller("couponController",["$scope","$location","$routeParams","CONSTANT","feWebService","apiConfig","commonService","feCache","feToast",function(e,t,n,a,i,o,r,s,c){var l=n.activity_id;if(!l)return void r.goErrorMsg({},!0);e.show={contents:!1,end:!1};var u={data:{activity_id:l}};i.http(angular.extend(u,o.coupon.getActivityInfo)).then(function(n){if(n&&n.data){var i=n.data.free_coupons_id;if("Y"!=n.data.is_start)return void t.path("/coupon/notice").search({type:"03",start_time:n.data.start_time});if(s.uc.put(a.CACHE.COUPON_ACTIVITY,n.data),e.show.content=!0,n.data.max_loan_amt&&n.data.effective_days>0){if(e.activity={useRule:n.data.max_loan_amt,effectiveDays:n.data.effective_days,faceValue:n.data.face_value},"Y"==n.data.is_end)e.show.end=!0;else if(i)return void t.path("/coupon/share").search({free_coupons_id:i,force:!0})}else r.goErrorMsg({},!0)}else r.goErrorMsg({},!0)},function(){r.goErrorMsg({msg:"暂时无法获取活动信息"},!0)}),e.btn={seize:function(){i.http(angular.extend(u,o.coupon.seize)).then(function(n){if(n&&n.data){var i=n.data.result;switch(i){case"0":var o=s.uc.get(a.CACHE.COUPON_ACTIVITY);s.uc.put(a.CACHE.COUPON_ACTIVITY,angular.extend(o,n.data)),t.path("/coupon/share").search({free_coupons_id:n.data.free_coupons_id});break;case"1":t.path("/coupon/empty").search({type:"01"});break;case"2":e.show.end=!0;break;case"3":c.showMask("您已参与过本活动",3e3);break;case"99":c.showMask("当前参与活动的人太多，请退出后重新进入领取页面",3e3);break;default:r.goErrorMsg({},!0)}}else r.goErrorMsg({},!0)})}}}]),app.controller("couponListController",["$scope","$routeParams","$location","$q","CONSTANT","feUtils","apiConfig","feConfirm","envConfig","commonService","commonConfig","feWebService","couponService","productService","PageList","feToast","feSelectPopup","feSession","browserService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h){var C=t.free_coupons_type,b=t.subType||o.getParamFromURL("subType"),y=i.COUPON.FREE_COUPONS_TYPE,w=i.COUPON.QUERY_DIMENSION_TYPE,S=JSON.parse(v.get(u.cache.session.cpmm_route_params.key)||"{}"),E=null;e.entList=[],e.currentEnterprise={},e.isFromLimitPage=b===i.APP.SUB_TYPE.MANAGE;var T=p.isNN();T&&h.filter({handleBack:"close"});var A=function(){var e=[{label:"未绑定",couponType:y.UNBIND,couponListClass:"unbond"},{label:"待使用",couponType:y.UNUSED,couponListClass:"unused"},{label:"不可用",couponType:y.UNAPPLICABLE,couponListClass:"unvailable"}];return e},N=function(){for(var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:[],t=0,n=0,a=e.length;n<a;n++)e[n].couponType===C&&(t=n);return t},I=function(e){return!T&&"02"===e},x=function(){var e={};return e.tabs=A(),e.currentIndex=N(e.tabs),e.showGoLoanBtn=I(e.tabs[e.currentIndex].couponType),e};e.navData=x(),e.couponsData={list:[],couponListEmpty:!0,couponListClass:"unbond"},e.evt={changeNav:function(t){var n=e.navData;(!t||t>n.length-1)&&(t=0),e.navData.currentIndex=t,e.navData.showGoLoanBtn=I(e.navData.tabs[t].couponType),L()},bindCoupon:function(t){e.isFromLimitPage?p.bindCoupon(t,S.ccif).then(function(){e.evt.changeNav(e.navData.currentIndex+1),g.show(u.msg.bindCouponSuccess),e.evt.selectCoupon(t)}):p.getEntList(t).then(function(n){var a=n.filter(function(e){return e.valid}).map(function(e){return{value:e.ccif_no,text:e.org_name}});a.length<=0&&s.show({content:u.msg.noCouponApplyCompany,btn:{primary:{text:"去申请",click:function(){s.hide(),l.goMesp()}}}}),_.show({current:-1,options:a,click:function(n){_.current=n.value,p.bindCoupon(t,n.value).then(function(){P(n),_.hide(),e.evt.changeNav(e.navData.currentIndex+1),g.show(u.msg.bindCouponSuccess),e.evt.selectCoupon(t)})},title:"选择免息券绑定企业"})})},selectCoupon:function(t){e.selectedCouponCode==t?e.selectedCouponCode="":e.selectedCouponCode=t},goLoan:function(){l.goMesp({cpmmEntryType:"loanApply",cpmmFreeCouponId:e.selectedCouponCode})},changeBindEnterprise:function(){_.show({current:e.currentEnterprise.value,options:e.entList,click:function(e){P(e),_.hide(),L()},title:"更换企业"})}};var L=function(){E=null,k(),e.couponsData.couponList.nextPage()},P=function(t){e.currentEnterprise=t?t:e.entList[0]||{},_.current=e.currentEnterprise.value},k=function(){var t=e.navData.tabs[e.navData.currentIndex].couponType,n={free_coupons_type:t};t===y.UNBIND?n.query_dimension_type=w.USER:t===y.UNUSED?(n.query_dimension_type=w.ENTERPRISE,n.ccif=e.currentEnterprise.value):(n.query_dimension_type=e.isFromLimitPage?w.ENTERPRISE:w.USER_WITH_ENTERPRISE,n.ccif=e.currentEnterprise.value),e.isFromLimitPage&&S.ccif&&(n.ccif=S.ccif,n.product_id=S.product_id,n.com_product_id=S.com_product_id,n.project_id=S.project_id),e.couponsData.couponList=new f({pageSize:10,pageNumber:1,requestParam:angular.extend(r.coupon.getCouponList,{data:n}),transformRequest:function(){return n.start_index=E,n.page_size=10,delete n.pageNumber,n},transformResponse:function(t){return e.isFromLimitPage&&t.enterpriseName&&(e.currentEnterprise={text:t.enterpriseName}),E=t.nextIndex,p.formatCouponListData(t.list,n.free_coupons_type)},successCallback:function(){this.isEnd=!E,e.couponsData.couponListEmpty=e.couponsData.couponList.items.length<=0},errorCallback:function(){}}),e.couponsData.couponListClass=e.navData.tabs[e.navData.currentIndex].couponListClass};e.isFromLimitPage?k():p.getEntList().then(function(t){e.entList=t.map(function(e){return{value:e.ccif_no,text:e.org_name}}),P(),k(),e.couponsData.couponList.refresh()})}]),app.controller("couponListForWpmController",["$scope","$routeParams","CONSTANT","apiConfig","envConfig","commonService","feWebService","couponService","productService","PageList",function(e,t,n,a,i,o,r,s,c,l){var u=null;e.submitBtnText="去借款",e.couponsData={list:[],couponListEmpty:!1,couponListClass:"unbond"},e.evt={bindCoupon:function(t){s.bindCoupon(t).then(function(){e.selectedCouponCode=t,e.evt.goLoan()})},shareCoupon:function(e){s.showCouponShareLayer({couponId:e})},selectCoupon:function(t){e.selectedCouponCode==t?e.selectedCouponCode="":e.selectedCouponCode=t},goLoan:function(){s.goWpmLoanApply({cpmmFreeCouponId:e.selectedCouponCode})}};var d={free_coupons_type:n.COUPON.FREE_COUPONS_TYPE.VALID,query_dimension_type:n.COUPON.QUERY_DIMENSION_TYPE.USER_WITH_ENTERPRISE};e.couponsData.couponList=new l({pageSize:10,pageNumber:1,requestParam:angular.extend(a.coupon.getCouponList,{data:d}),transformRequest:function(){return d.start_index=u,d.page_size=10,delete d.pageNumber,d},transformResponse:function(e){return u=e.nextIndex,s.formatCouponListData(e.list)},successCallback:function(){this.isEnd=!u,e.couponsData.couponListEmpty=e.couponsData.couponList.items.length<=0},errorCallback:function(){}})}]),app.controller("noticeController",["$scope","$routeParams","CONSTANT","apiConfig","commonService","couponService","feToast","feWebService","feUtils","fePageService","feCache","publicConfig",function(e,t,n,a,i,o,r,s,c,l,u,d){var p=t.is_self||"Y";e.WeChatSubsription=d.WeChatSubsription;var m=function(t){var n={data:{free_coupons_id:t}};s.http(angular.extend(n,a.coupon.share)).then(function(t){if(t&&t.data){var n=t.data.result;if("0"==n){var a=o.getShareUrl(e.free_coupons_id,t.data.transfer_id);e.url=a}else"1"==n||"2"==n?r.show("您的免息券已被领取"):i.goErrorMsg({msg:"分享失败"},!0)}})},f={"01":{title:"该券已过期或已被绑定",content:"抱歉，该"+(d.productName2||"")+"免息券已过期或已被绑定。您可以关注"+d.WeChatSubsription+"，参与相关活动获取免息券。",show:!1,isShare:!1},"02":{title:"您已经分享过该免息券",content:"您已经分享过该免息券，若需分享给其他好友，请出示以下二维码，或点击立即分享进行重新分享。",show:!0,isShare:!0},"03":{show:!1,isShare:!1}},g=t.type;if(!g)return void i.goErrorMsg({},!0);if(!f.hasOwnProperty(g))return void i.goErrorMsg({},!0);if(e.notice=f[g],"02"==g){e.free_coupons_id=t.free_coupons_id,m(e.free_coupons_id);var _=u.uc.get(n.CACHE.COUPON_ACTIVITY);if(_)e.faceValue=_.face_value;else{var v={data:{free_coupons_id:e.free_coupons_id,transfer_id:t.transfer_id}};s.http(angular.extend(v,a.coupon.queryCoupon)).then(function(t){var n=t.data;"1"==n.free_coupons_status||"0"==n.free_coupons_status?e.faceValue=n.face_value:"2"==n.free_coupons_status?$location.path("/coupon/share").search(params):"3"==n.free_coupons_status?$location.path("/coupon/notice").search({type:"01"}):i.goErrorMsg({},!0)})}}else"03"==g&&(t.start_time?e.notice.title="该活动开始时间为<br>"+c.formatDate(t.start_time)+"<br>敬请关注!":i.goErrorMsg({msg:"缺少活动开始日期"},!0));e.btn={share:function(){if(e.free_coupons_id){var t={couponId:e.free_coupons_id};e.url&&(t=angular.extend(t,{url:e.url})),o.showCouponShareLayer(t)}else i.goErrorMsg({},!0)}},l.setPageTitle("Y"==p?"免息券":"领取结果")}]),app.controller("couponShareController",["$scope","$routeParams","$location","CONSTANT","apiConfig","commonService","feCache","feWebService","feQRCode","feCookie","commonConfig","weixinService","feUtils","couponService","fePageService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f){var g={SELF_NORMAL:{title:"",beReceived:!1,unable:!1,btn:{enable:!0,text:"立即申请"},pageTitle:"免息券"},SELF_UNABLE:{title:"",beReceived:!0,unable:!0,btn:{enable:!0,text:"立即申请"},pageTitle:"免息券"},OTHER_NORMAL:{title:"已领取",beReceived:!1,unable:!1,btn:{enable:!0,text:"立即申请"},pageTitle:"领取结果"},OTHER_UNABLE:{title:"领取失败",beReceived:!1,unable:!0,btn:{enable:!1,text:"已被抢走"},pageTitle:"领取结果"}};if(e.couponId=t.free_coupons_id,!e.couponId)return void o.goErrorMsg({},!0);var _=t.transfer_id;e.activity=r.uc.get(a.CACHE.COUPON_ACTIVITY);var v=function(){var a=e.activity.free_coupons_status;a?"99"==a||"0"==a?o.goErrorMsg({msg:"异常免息券类型"},!0):"3"==a?n.path("/coupon/notice").search({type:"01",is_self:e.activity.is_self}):!_||e.activity.is_self&&"Y"==e.activity.is_self?"1"==a?e.status=g.SELF_NORMAL:"2"==a?e.status=g.SELF_UNABLE:o.goErrorMsg({msg:"异常免息券类型"},!0):"1"==a?(e.status=g.OTHER_NORMAL,t.force&&(e.status.title="领取成功")):"2"==a?e.status=g.OTHER_UNABLE:o.goErrorMsg({msg:"异常免息券类型"},!0):e.status=g.SELF_NORMAL,e.status&&f.setPageTitle(e.status.pageTitle)};if(!e.activity||t.force){var h={data:{free_coupons_id:e.couponId,transfer_id:_}};s.http(angular.extend(h,i.coupon.queryCoupon)).then(function(t){t&&t.data&&(e.activity=t.data,v())})}else v();e.btn={share:function(){var t=e.couponId;m.showCouponShareLayer({couponId:t})},goMesp:function(){e.status.btn.enable&&o.goMesp()}}}]),app.controller("exchangeDetailController",["$scope","$location","apiConfig","CONSTANT","feCache","feWebService","feUI","feConfirm","feAnalyticsService","exchangeDetailService",function(e,t,n,a,i,o,r,s,c,l){var u=e.prize=i.uc.get(a.CACHE.GIFT_EXCHANGE);u||t.path("/mgm/giftExchange"),e.showAddBtn=!1,e.showDefautAddress=!1,e.defaultAddress={city_name:"",address_id:"",province_name:"",district_name:"",street_detail:""},e.button=new r.Button({text:"确定兑换",disabled:!0,click:function(){s.show({title:"确定使用"+u.worth_coins+"奖励金<br/>兑换"+u.award_name+"奖品?",content:"确定兑换后将扣除相应兑换的奖励金，且地址不可修改，订单不可取消，确认您的订单信息无误",contentLeft:!0,btn:{primary:{text:"确认兑换",click:function(){c.clickStat(c.EVENT_NAME.BUTTON_CLICK,"mgm_exchangeDetail_confirmBtn"),
// s.hide();var a=e.defaultAddress,i=[a.province_name,a.city_name,a.district_name,a.street_detail].join(" "),r={data:{award_id:u.award_id,address_id:a.address_id,address_detail:i,activity_id:u.activity_id}};o.http(angular.extend(r,n.gift.exchangedAward)).then(function(e){var n=e.data;if(n)return 0==n.exchangedResult?void t.path("/mgm/exchangeStatus").search({exchangedResult:n.exchangedResult}):1==n.exchangedResult?(l.setAwardName(u.award_name),void t.path("/mgm/exchangeStatus").search({exchangedResult:n.exchangedResult})):void 0})}},hide:{text:"暂不兑换"}}})}}),e.goEditAddress=function(){t.path("/address/editAddress").search({type:1})},e.goManageAddress=function(){c.clickStat(c.EVENT_NAME.BUTTON_CLICK,"mgm_exchangeDetail_goManageAddressBtn"),t.path("/address/manageAddress")};var d=function(){var t={data:{pageNumber:1,pageSize:1,isDefault:"Y"}};o.http(angular.extend(t,n.address.getAddressList)).then(function(t){t.data&&t.data.list&&t.data.list.length>0?(e.defaultAddress=t.data.list[0],e.showDefautAddress=!0,e.button.disabled=!1):e.showAddBtn=!0})},p=function(){var t=l.getChooseAddress();t&&null!=t?(e.defaultAddress=t,e.showDefautAddress=!0,e.button.disabled=!1):d()};0==u.award_type?(e.isVirtualPrize=!0,e.button.disabled=!1):p()}]),app.factory("exchangeDetailService",function(){var e="",t=null,n=function(t){e=t},a=function(){return e},i=function(e){t=e},o=function(){return t};return{setAwardName:n,getAwardName:a,setChooseAddress:i,getChooseAddress:o}}),app.controller("exchangeStatusController",["$scope","$routeParams","$location","weixinService","feUI","exchangeDetailService","CONSTANT",function(e,t,n,a,i,o,r){var s=t.exchangedResult;if(e.exchangeStatus={outAwardName:""},1==s){var c=o.getAwardName();e.exchangeStatus.outAwardName=c?c:"该"}var l=[{icon:"ui-icon-warn",title:e.exchangeStatus.outAwardName+"奖品已被兑换完<br>请兑换其他奖品"},{icon:"ui-icon-success",title:"奖品兑换成功",msg:'请等待发放，我们将在一个月内发放完成<br>如有疑问，可拨打：<a href="tel:'+r.APP.HOTLINE+'">'+r.APP.HOTLINE+"</a>"}];e.ui=1==s?l[0]:l[1],e.button=new i.Button({text:1==s?"返回兑换奖品":"确认",click:function(){1==s?n.path("/mgm/giftExchange"):n.path("/mgm/giftExchange")}})}]),app.controller("exchangedListController",["$scope","$location","$routeParams","commonConfig","feCookie","apiConfig","feUI","CONSTANT","PageList",function(e,t,n,a,i,o,r,s,c){var l,u=(l={},_defineProperty(l,s.APP.EXCHANGEDSTATUS.SHIPPED,{val:"已发出",color:"gray"}),_defineProperty(l,s.APP.EXCHANGEDSTATUS.NOTSHIPPED,{val:"待发出",color:"orange"}),l);e.goExchangeBtn=new r.Button({text:"去兑换",click:function(){t.path("/mgm/giftExchange")}});var d=a.cache.cookie,p=i.get(d.mgm_activity_id.key);angular.extend(o.gift.getExchangedAwards,{data:{activity_id:p}}),e.exchangeList=new c({requestParam:o.gift.getExchangedAwards,transformResponse:function(t){t&&t.list&&t.list.length?e.notEmpty=!0:e.empty=new r.EmptyList({isEmpty:!0,title:"当前没有兑换记录",iconClass:" "});var n=!0,a=!1,i=void 0;try{for(var o,s=(t.list||[])[Symbol.iterator]();!(n=(o=s.next()).done);n=!0){var c=o.value;c.statusVal=u[c.status].val,c.statusColor=u[c.status].color,"0"==c.award_type&&(c.statusVal="待使用")}}catch(e){a=!0,i=e}finally{try{!n&&s.return&&s.return()}finally{if(a)throw i}}return t.list}})}]),app.controller("giftExchangeController",["$scope","$location","CONSTANT","commonConfig","feUtils","feCookie","apiConfig","feUI","feCache","feWebService","feToast","PageList","marketingService","feAnalyticsService","exchangeDetailService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f){var g=void 0;e.notZero=!1,e.canExchange=!1,e.awardValid=!0,e.goExchangeHistory=function(){m.clickStat(m.EVENT_NAME.BUTTON_CLICK,"mgm_giftExchange_goExchangeListBtn"),t.path("/mgm/exchangedList")},e.goExchangeDetail=function(){g?(m.clickStat(m.EVENT_NAME.BUTTON_CLICK,"mgm_giftExchange_goExchangeDetailBtn"),c.uc.extend(n.CACHE.GIFT_EXCHANGE,g),f.setChooseAddress(null),t.path("/mgm/exchangeDetail")):u.show(a.msg.plsChooseGift)},e.check=function(t){e.giftList.items[t].giftStatus[1]&&(m.clickStat(m.EVENT_NAME.BUTTON_CLICK,"mgm_giftExchange_checkBtn"),g&&(g.checked=!1),e.giftList.items[t].checked=!0,g=e.giftList.items[t])};var _=function(){var t=a.cache.cookie,n=o.get(t.mgm_activity_id.key);l.http(angular.extend({data:{activity_id:n}},r.activity.getActivityInfo)).then(function(t){if(e.mgmUserInfo=t.data,e.mgmUserInfo.award_coins_expire_time=i.formatDate(e.mgmUserInfo.award_coins_expire_time),0==t.data.current_award_coins){var a="Y"==t.data.is_end;e.empty=new s.EmptyList({isEmpty:!0,title:"没有奖励金可兑换奖品",iconClass:"",desc:"快去邀请小伙伴们加入<br>即可获得奖励金兑换奖品",button:new s.Button({text:"立即邀请",show:!0,disabled:!!a,click:function(){a||p.showMgmShareMenu()}})})}else e.notZero=!0,angular.extend(e.giftList.requestParam,{data:{activity_id:n}}),e.giftList.refresh(),"Y"==t.data.is_expired&&(e.canExchange=!1,e.awardValid=!1)})},v=function(){e.giftList=new d({requestParam:r.gift.getAvailableAwards,autoLoad:!1,transformResponse:function(t){if(!t||!t.list)return[];e.notZero&&(t.list.length?(e.notEmpty=!0,e.awardValid&&(e.canExchange=!0)):e.empty=new s.EmptyList({isEmpty:!0,title:"没有可兑换奖品",iconClass:"ui-icon-banner-empty"}));var n=e.mgmUserInfo.current_award_coins,a=!0,i=!1,o=void 0;try{for(var r,c=(t.list||[])[Symbol.iterator]();!(a=(r=c.next()).done);a=!0){var l=r.value,u=l.worth_coins-n;l.giftStatus=[!1,!1,!1],l.gap=u,l.checked=!1,0==l.award_status?l.giftStatus[2]=!0:u>0?l.giftStatus[0]=!0:l.giftStatus[1]=!0}}catch(e){i=!0,o=e}finally{try{!a&&c.return&&c.return()}finally{if(i)throw o}}return t.list}})},h=function(){_(),v()};h()}]),app.controller("newLeaveController",["$scope","$location","$routeParams","feWebService","apiConfig","commonService","feUtils","feUI","feLoading","feEnvService","envConfig","$http","commonConfig","feCookie","feCache","feSession","CONSTANT","feToast","$timeout","$sce","weixinService","marketingService","feAnalyticsService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h,C,b,y,w){var S=p.cache.session.page_data_session_key.key,E=g.get(S);E=E?JSON.parse(E):{},g.remove(S)}]),app.controller("leaveStaticforDHDController",["$scope","$location","$routeParams","feWebService","apiConfig","commonService","feUtils","feUI","feLoading","feEnvService","envConfig","$http","commonConfig","feCookie","feCache","feSession","CONSTANT","feToast","$timeout","$sce","weixinService","marketingService","feAnalyticsService","leaveMessageService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h,C,b,y,w,S){function E(){a.http(angular.extend({},i.activity.scrollingDisplay)).then(function(t){if(t.data&&t.data.list){var n=t.data.list;if(Array.isArray(n)&&n.length>0)for(var a=0;e.scrollList.length<10;){n[a]||(a=0);var i=JSON.parse(JSON.stringify(n[a]));i.timeStr=R[a],i.creditAmt=Math.floor(i.creditAmt/1e4),e.scrollList.push(i),a++}}})}function T(){var t=e.input.receiverMobile.val;return r.isValidPhoneNumber(t)?(e.btn.otp.countdown(),void a.http(angular.extend({data:{phone_no:t,advert_id:e.advertId}},i.common.getOtp)).then(function(e){})):void v.show(p.msg.mobileNumError)}function A(t){e.diologShow=!0,e.tips=t}function N(){if(!e.advertId||!e.channelId)return void A("留资ID或渠道号获取不到！");if(I()){var t={data:{product_brand_name:e.input.brandName.val,brand_name:e.input.brandName.val,enterprise_name:e.input.enterprisName.val,contact_name:e.input.contactName.val,contact_phone_no:e.input.receiverMobile.val,channel_id:e.channelId,advert_id:e.advertId,efs_channel_id:e.efs_channel_id,url:e.url,request_id:e.requestId,otp_code:e.input.otp.val,activity_id:U,inviter_union_id:D}};w.bpButtonClick("dhd_assetsLeaveMessage_submit"),a.http(angular.extend(t,i.advertisement.submitConsumerInfo)).then(function(e){window.location.href=r.appendParamsToRouter(u.mgm2Prefix+"result/dhd",{mgmActivityId:U})},function(e){A(e.msg)})}}function I(){var t=!0;for(var n in e.input)if(e.input.hasOwnProperty(n)&&e.input[n].validate&&(e.input[n].validate(),!e.input[n].isValid)){t=!1;break}return t}e.consumerHotline="0755-88882018";var x="asset/images/ad/serive_icon.png";e.seriveTelOrd=r.getCDNUrl()?r.getCDNUrl()+x:"./"+x;var L=p.cache.session.page_data_session_key.key,P=g.get(L);P||o.goErrorMsg({},!0),P=P?JSON.parse(P):{};var k=p.cache.cookie,O=m.get(k.brand_name.key);e.advertId=n.advert_id,e.channelId=n.channel_id,e.efs_channel_id=n.efs_channel_id,e.isCompanyShow=!1,e.selectList=[],e.scrollList=[];var D=m.get(k.invite_union_id.key),U=m.get(k.mgm_activity_id.key),M=void 0,R=["56秒前","1分钟前","3分钟前","5分钟前","8分钟前","10分钟前","15分钟前","半小时前","一小时前","两小时前"];e.input={brandName:new s.Input({lab:"所属品牌",val:O,placeholder:"请选择",containerClass:"",warningClass:"opt-warning",inputClass:"part-con",labelClass:"part-title",itemClass:"input-part",maxlength:50,validation:{validator:function(e){return""!=e&&""!=e.trim()},err:"所属品牌不能为空"},blur:function(){S.setTopValue()}}),receiverMobile:new s.InputMobile({lab:"联系人手机号",val:"",placeholder:"请输入",containerClass:"",warningClass:"opt-warning",inputClass:"part-con",labelClass:"part-title",itemClass:"input-part",blur:function(){S.setTopValue()}}),enterprisName:new s.Input({lab:"",val:"",placeholder:"请输入营业执照上企业全称",containerClass:"",inputClass:"part-con",labelClass:"",itemClass:"part-enterpris",maxlength:50,validation:{validator:function(e){return void 0!=e&&""!=e&&""!=e.trim()},err:"企业名称不能为空"},change:function(){var t=e.input.enterprisName.val.length;if(M&&h.cancel(M),e.input.enterprisName.val||(e.isCompanyShow=!1),t>0){var n=[],o={data:{root:e.input.enterprisName.val}};e.input.receiverMobile.val&&r.isValidPhoneNumber(e.input.receiverMobile.val)&&(M=h(function(){a.http(angular.extend(o,i.advertisement.getAssociatedNames)).then(function(t){n=t.data.list,n.length>0&&(e.selectList=n,e.isCompanyShow=!0)})},400)),e.onItemDown=function(e){event.preventDefault()},e.onItemClick=function(t){e.isCompanyShow=!1,e.input.enterprisName.val=n[t]}}else e.isCompanyShow=!1},blur:function(){e.isCompanyShow=!1,S.setTopValue()}}),contactName:new s.Input({lab:"法人代表姓名",val:"",placeholder:"请填写",containerClass:"",warningClass:"opt-warning",inputClass:"part-con",labelClass:"part-title",itemClass:"input-part",maxlength:50,validation:{validator:function(e){return void 0!=e&&""!=e&&""!=e.trim()},err:"法人代表不能为空"},blur:function(){S.setTopValue()}}),otp:new s.InputOTP({lab:"验证码",placeholder:"请输入验证码",containerClass:"",inputClass:"part-con",labelClass:"part-title",itemClass:"input-part",warningClass:"opt-warning",change:function(){this.validate()},blur:function(){S.setTopValue()}})},e.btn={otp:new s.ButtonOTP({click:function(){return!this.disabled()&&void T()}}),submit:function(){N()},diologPrimary:function(){e.diologShow=!1}},e.onItemDown=function(e){event.preventDefault()},e.onItemClick=function(t){e.isCompanyShow=!1,e.input.enterprisName.val=arr[t]},e.choice=function(){m.set(k.advert_id.key,e.advertId);f.uc.extend(_.CACHE.BRAND_NAME,{enterprise_name:e.input.enterprisName.val,contact_name:e.input.contactName.val,contact_phone_no:e.input.receiverMobile.val}),t.path("/advertisement/brandName").search(Object.assign(n,{page_type:"asset"}))};var $=f.uc.get(_.CACHE.ADVERTISEMENT)||{};$&&(e.input.enterprisName.val=$.enterprise_name,e.input.contactName.val=$.contact_name,e.input.receiverMobile.val=$.contact_phone_no),E()}]),app.controller("marketingLoginController",["$scope","$routeParams","CONSTANT","commonConfig","marketingService","commonService","feUtils","feCookie","feSession","feAnalyticsService",function(e,t,n,a,i,o,r,s,c,l){var u=t.activity_type,d="Y"===t.isEsp;d&&c.set(n.CACHE.IS_ESP,t.isEsp);var p=t.from;u||o.goErrorMsg({msg:"无法获取活动类型"},!0),"mgmBanner"==p&&l.bpButtonClick("from_mgm_banner_link");var m={data:{code:r.getParamFromURL("code"),client_sys_info:r.getClientSysInfo()}};i.login(m,t)}]),app.controller("marketingWeixinLoginController",["$scope","$routeParams","feAlert","apiConfig","commonService","feWebService","feUtils","marketingService","weixinService",function(e,t,n,a,i,o,r,s,c){var l=t.activity_type;l||i.goErrorMsg({msg:"无法获取活动类型"},!0);var u=o.getLocalFileAbsUrl(a.activity.marketingLoginRoute);u=r.appendParamsToRouter(u,t);var d=c.getWeChatOAuthUrl(u);window.location.href=d}]),app.controller("achievementWallController",["$scope","$location","feLoading","feSession","feToast","commonConfig","feWebService","apiConfig","medalService",function(e,t,n,a,i,o,r,s,c){function l(){u()}function u(){r.http(s.medal.getMedalList).then(function(t){var n=t.data||{},a=n.medal_list||[];n.have_num=n.have_num||0,n.total_num=n.total_num||0,n.lightAll=n.have_num===n.total_num,a.forEach(function(e){c.formatMedalInfo(e)}),e.medalsInfo=n})}function d(e){try{var t=document.createElement("canvas");t.width=e.width,t.height=e.height;var n=t.getContext("2d");return n.drawImage(e,0,0,e.width,e.height),t.toDataURL("image/png")}catch(e){}}var p="/cpmm-amfront/medal/shareImg.json",m="";l(),e.showMedalInfo=function(e){a.set(o.cache.session.medal_info.key,JSON.stringify(e)),t.path("/medal/medalInfo")},e.achieveWallDialogInfo={showPrizeInfo:!1,show:function(){n.show();var t=new Image;t.onload=function(){m=d(t),n.hide(),m&&(e.achieveWallDialogInfo.dialogStyle={width:t.width/2.2+"px",height:t.height/2.2+"px"},e.achieveWallDialogInfo.imgSrc=m,e.achieveWallDialogInfo.showPrizeInfo=!0,e.$apply())},t.onerror=function(){n.hide(),i.show("生成成就墙失败，请稍候再试~"),e.$apply()},t.src=p},imgSrc:p,dialogStyle:{}}}]),app.factory("medalService",["$location","CONSTANT","feUtils","envConfig","feFloatUtil",function(e,t,n,a,i){function o(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},n=arguments.length>1&&void 0!==arguments[1]?arguments[1]:"list",a="Y"===e.user_have_flag;e.isLight=a,a?e.lightImg="list"===n?e.img_small_bright_url:e.img_big_bright_url:e.unLightImg="list"===n?e.img_small_gray_url:e.img_big_gray_url,e.strategyList=d(e),e.hasStrategyList=e.strategyList.length>0,r(e);var i=e.strategy_status===t.MEDAL.STRATEGY_STATUS.NOT_STARTED?e.strategy_begin_time:e.strategy_end_time;e.strategyTime=u(i),e.statusImg=!a&&E[e.strategy_status],e.showStrategyTime=!!e.showStrategyTime&&!(e.hasStrategyList&&e.btnInfo&&e.btnInfo.show)}function r(e){var a,i=!e.isLight&&e.strategy_status===t.MEDAL.STRATEGY_STATUS.IN_PROGRESS,o=(a={},_defineProperty(a,t.MEDAL.MEDAL_THIRD_TYPE.STEP_ENTERPRISE_VERIFY,function(){return{medal_tips:s(e,{progress:_,notStartTip:_}),btnInfo:p(e)}}),_defineProperty(a,t.MEDAL.MEDAL_THIRD_TYPE.STEP_FIRST_CREDIT_SUCC,function(){return{medal_tips:s(e,{progress:v}),btnInfo:p(e),showStrategyTime:!e.isLight&&e.strategy_status===t.MEDAL.STRATEGY_STATUS.CLOSED}}),_defineProperty(a,t.MEDAL.MEDAL_THIRD_TYPE.STEP_FIRST_LOAN_SUCC,function(){return{medal_tips:s(e,{progress:h}),btnInfo:p(e)}}),_defineProperty(a,t.MEDAL.MEDAL_TYPE.MGM,function(){return e.strategyList.push({award_type:"P",point_num:e.recommend_succ_point,urlType:"MGM_MALL"}),e.hasStrategyList=!1,{medal_tips:s(e,{getTip:"您已成功邀请"+e.recommend_succ_num+"位好友",progress:C},"MGM"),btnInfo:{show:e.isLight||e.strategy_status!==t.MEDAL.STRATEGY_STATUS.CLOSED,disabled:e.strategy_status===t.MEDAL.STRATEGY_STATUS.NOT_STARTED},mgmStrategyList:d(e),strategyList:[]}}),_defineProperty(a,t.MEDAL.MEDAL_TYPE.SUM_LOAN_AMT,function(){return{progressInfo:{show:i,progressText:l(e.sum_loan_amt)+"万",progress:n.toPercent(e.sum_loan_amt,e.pass_sum_loan_amt)},medal_tips:s(e,{progress:"再借"+l(Math.max(e.pass_sum_loan_amt-e.sum_loan_amt,0))+"万即可获得勋章",notStartTip:"借款"+l(e.pass_sum_loan_amt)+"万即可获得勋章"},"DELAY"),btnInfo:p(e)}}),_defineProperty(a,t.MEDAL.MEDAL_TYPE.SUM_LOAN_DAYS,function(){return{progressInfo:{show:i,progressText:e.sum_loan_days+"天",progress:n.toPercent(e.sum_loan_days,e.pass_sum_loan_days)},btnInfo:{show:!1},medal_tips:s(e,{progress:"您已累计用款"+e.sum_loan_days+"天，再正常借款"+Math.max(e.pass_sum_loan_days-e.sum_loan_days,0)+"天且不提前还款，即可获得勋章",notStartTip:"累计用款"+e.pass_sum_loan_days+"天不提前还款，获得勋章"},"DELAY")}}),_defineProperty(a,t.MEDAL.MEDAL_TYPE.USED_CREDIT_AMT_RATE,function(){var t=e.pass_used_credit_amt_rate,n=e.total_credit_amt*t,a=Math.max(0,n-e.used_credit_amt),o=n<=0?0:e.used_credit_amt/n,r="您当前用款"+l(e.used_credit_amt)+"万，再借"+l(a)+"万即可获得勋章";return e.total_credit_amt<=0&&(r="您还未核额成功",e.button_text="去申请核额",i=!1),{medal_tips:s(e,{progress:r,notStartTip:"额度使用率达"+100*Number(t)+"%即可获得勋章"},"DELAY"),btnInfo:p(e),progressInfo:{show:i,progress:Math.min(100,Math.floor(100*o))+"%"}}}),_defineProperty(a,t.MEDAL.MEDAL_TYPE.ONE_LOAN_KEEP_DAYS,function(){return{medal_tips:c(e)}}),a),r="STEP"===e.second_type?e.third_type:e.second_type,u=o[r]||function(){return{}},m=u();angular.extend(e,m),e.medalType=r}function s(e,n){var a=arguments.length>2&&void 0!==arguments[2]?arguments[2]:"STEP",i=n.progress,o=n.notStartTip||i,r=n.getTip||f,s=e.isLight,c=S[a],l="",u="",d="";return s?l=r:e.strategy_status===t.MEDAL.STRATEGY_STATUS.CLOSED?(u=b,l=g,d=c.CLOSED):e.strategy_status===t.MEDAL.STRATEGY_STATUS.IN_PROGRESS?(u=w,d=c.IN_PROGRESS,l=i):(u=y,l=o),e.showStrategyTime=!s,e.strategyTimeTips=u,e.medalTipText=d,l}function c(e){var a=S.DELAY,i=e.strategy_status,o=e.curr_loan_keep_days,r=e.pass_one_loan_keep_days,s=e.strategy_end_time,c=e.local_time,l=t.MEDAL.STRATEGY_STATUS,d=u(e.strategy_end_time),p={show:!1,progressText:e.pass_one_loan_keep_days+"天",progress:n.toPercent(e.curr_loan_keep_days,e.pass_one_loan_keep_days)},m={hasLoan:"您已连续使用"+o+"天，再坚持使用"+Math.max(r-o,0)+"天且不提前还款，即可获得勋章",needLoan:"借一笔"+r+"天的借款不提前还款即可获得勋章",noEligible:"获得勋章需要在"+d.year+"年"+d.month+"月"+d.day+"日前完成连续"+r+"天的借款，您暂不符合资格",unStart:"单笔借款连续用款"+r+"天不提前还款且不逾期即可获得勋章"},_="",v=!1,h="",C="";return e.isLight?_=f:i===l.CLOSED?(v=!0,_=g,h=b,C=a.CLOSED):i===l.NOT_STARTED?(v=!0,_=m.unStart,h=y):i===l.IN_PROGRESS&&(C=a.IN_PROGRESS,e.loan_no?(_=m.hasLoan,e.showLoanNo=!0,p.show=!0):new Date(s).getTime()-new Date(c).getTime()>24*r*3600*1e3?(v=!0,e.btnInfo={show:!0,disabled:!1},_=m.needLoan,h=w,p.show=!0):_=m.noEligible),e.strategyTimeTips=h,e.showStrategyTime=v,e.medalTipText=C,e.progressInfo=p,_}function l(e){return i.toFixed(Number(e)/1e4,2,!1)}function u(e){var t=new Date(e),n=t.getFullYear(),a=t.getMonth()+1,i=t.getDate(),o=t.getHours(),r=t.getMinutes();return a=a>9?a:"0"+a,i=i>9?i:"0"+i,o=o>9?o:"0"+o,r=r>9?r:"0"+r,{year:n,month:a,day:i,hours:o,minutes:r}}function d(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},n="Y"===t.user_have_flag,i=(n?t.user_award_list:t.strategy_award_list)||[];return i.forEach(function(t){"C"===t.award_type?(t.text=t.coupons_name,t.class="coupon",t.click=function(){e.path("/coupon/list")}):"P"===t.award_type&&(t.text=t.point_num+"积分",t.class="point",t.click=function(){var e={MGM_MALL:a.wzMallUrl};window.location.href=e[t.urlType]||a.mallUrl}),t.btnText="去使用",t.btnHide=!n}),i}function p(e){return{show:!e.isLight&&(e.strategy_status===t.MEDAL.STRATEGY_STATUS.IN_PROGRESS||e.strategy_status===t.MEDAL.STRATEGY_STATUS.NOT_STARTED),disabled:e.strategy_status===t.MEDAL.STRATEGY_STATUS.NOT_STARTED}}var m,f="您已获得",g="未获得勋章",_="完成认证，获得勋章",v="完成首次核额成功，获得勋章",h="完成首次提款刷脸成功，获得勋章",C="成功推荐好友成功，得勋章和积分",b=["勋章发放已于","结束"],y=["勋章发放将于","开始"],w=["请于","前完成该勋章任务"],S={STEP:{IN_PROGRESS:"完成勋章任务后可实时获得勋章，如有延迟请耐心等待，如有积分或免息券奖励，先到先得，送完即止"},DELAY:{IN_PROGRESS:"当前勋章任务完成进度为截至昨日的数据，今日的完成进度请于明天查看，如有积分或免息券奖励，先到先得，送完即止",CLOSED:"当前勋章任务完成进度为截至昨日的数据"},MGM:{IN_PROGRESS:"完成勋章任务后可实时获得勋章，如有延迟请耐心等待"}},E=(m={},_defineProperty(m,t.MEDAL.STRATEGY_STATUS.NOT_STARTED,"asset/images/medal/medal_unstart.png"),_defineProperty(m,t.MEDAL.STRATEGY_STATUS.CLOSED,"asset/images/medal/medal_end.png"),m);return{formatMedalInfo:o}}]),app.directive("feMedalInfo",function(){return{restrict:"AE",transclude:!0,replace:!0,scope:{options:"="},template:' \n           <div class="medal-appeal">\n                <div class="item">\n                    <div class="item-header">\n                        <span class="item-hd-txt" ng-class="options.class">{{options.text}}</span>\n                        <button class="small-btn" ng-click="options.click()" ng-hide="options.btnHide">{{options.btnText}}</button>\n                    </div>\n<!--                    <div class="item-content" ng-transclude></div>-->\n                </div>\n            </div>',link:function(e){}}}).directive("feMedalTips",function(){return{restrict:"AE",transclude:!0,replace:!0,scope:{text:"="},template:'\n            <div class="medal-text-wrap">\n                <i class="line-left"></i>\n                <span class="medal-text" ng-transclude></span>\n                <i class="line-right"></i>\n            </div>\n            '}}).directive("achieveWallDialog",function(){return{restrict:"E",replace:!0,scope:{info:"="},template:'\n        <div class="we-dialog" ng-if="info.showPrizeInfo">\n            <div class="we-dialog__mask"></div>\n            <div class="pop-box medal-box" ng-style="{\'width\': info.dialogStyle.width}">\n                <div class="pop-btn">\n                    <button class="save-btn">长按图片区域可保存\n                        <div class="triangle"></div>\n                    </button>\n\n                    <i class="pop-close" ng-click="closeDialog()"></i>\n                </div>\n                <div class="pop-bd">\n                    <img class="medal-post" ng-style="info.dialogStyle" ng-src="{{info.imgSrc}}"/>\n                </div>\n            </div>\n            \n        </div>\n        ',controller:["$scope","$location","feAnalyticsService","envConfig",function(e,t,n,a){e.closeDialog=function(){e.info.showPrizeInfo=!1}}]}}),app.controller("medalInfoController",["$scope","$location","feSession","commonConfig","fePageService","feWebService","apiConfig","medalService","envConfig","commonService",function(e,t,n,a,i,o,r,s,c,l){function u(){o.http(angular.extend({data:{medal_id:p}},r.medal.getMedalDetail)).then(function(t){var n=t.data||{};n.local_time=n.local_time.replace(/-/g,"/"),n.strategy_begin_time=n.strategy_begin_time.replace(/-/g,"/"),n.strategy_end_time=n.strategy_end_time.replace(/-/g,"/"),s.formatMedalInfo(n,"detail"),e.medalInfo=n})}var d=JSON.parse(n.get(a.cache.session.medal_info.key)||""),p=d.medal_id;return p?(i.setPageTitle(d.medal_name),e.evt={goReceiptDetail:function(){e.medalInfo.loan_no&&l.goMespRoute({type:"receiptDetail",loan_acct_no:e.medalInfo.loan_no})},handleClick:function(){var t=e.medalInfo.button_url;t&&(window.location.href=t)}},void u()):void t.path("/common/msg")}]),app.controller("medalLoginController",["$scope","$routeParams","CONSTANT","$location","commonConfig","marketingService","commonService","feUtils","feCookie","feSession","apiConfig","feWebService",function(e,t,n,a,i,o,r,s,c,l,u,d){function p(){var e=c.get(i.cache.cookie.login_token.key),t={data:{}},o=e?angular.extend(t,u.red.getMgmUserInfo):angular.extend(m,u.red.login);d.http(o).then(function(e){l.set(n.CACHE.WE_PARTNER_REGISTER_STATUS,e.data.we_partner_register_status),l.set(i.cache.session.open_id.key,e.data.open_id),a.path("/medal").replace()},function(e){return!e||f>=1?void r.goErrorMsg(e,!1):(f++,void(r.isTokenError(e.status)&&m.code?(c.remove(i.cache.cookie.login_token.key,{path:i.cache.cookie.login_token.path}),p()):r.goErrorMsg(e,!0)))})}var m={data:{code:s.getParamFromURL("code")}},f=0;p()}]),app.controller("medalWeixinLoginController",["$scope","$routeParams","feWebService","feUtils","weixinService","apiConfig",function(e,t,n,a,i,o){var r=n.getLocalFileAbsUrl(o.medal.loginRoute);r=a.appendParamsToRouter(r,t),window.location.href=i.getWeChatOAuthUrl(r)}]),app.controller("newLeaveRouteController",["$scope","$location","$routeParams","feWebService","apiConfig","commonService","feUtils","feUI","feLoading","feEnvService","envConfig","$http","commonConfig","feCookie","feCache","feSession","CONSTANT","feToast","$timeout","$sce","weixinService","marketingService","feAnalyticsService","leaveMessageService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h,C,b,y,w,S){S.fetchPageInfo().then(function(e){e?t.path("/leavePage/"+e):t.path("/leaveMessage")})}]),app.controller("prizeListController",["$scope","$location","feCache","CONSTANT","PageList","envConfig","apiConfig","feUI","feSession","feAnalyticsService","feUtils","feCookie","commonConfig","commonService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m){function f(e){var t=e.split(" "),n=t[0].split("-"),a=n[0]+"年"+n[1]+"月"+n[2]+"日";return a}var g=10,_=function(){return angular.extend({data:{}},r.red.queryAwardsList)},v={data:{}};l.clickStat(l.EVENT_NAME.ELEMENT_SHOW,"red_prizeListPageShow"),e.awardsList=new i({pageSize:g,requestParam:_(v),transformRequest:function(e){return{page_start:e.pageNumber,page_size:e.pageSize}},transformResponse:function(t){return t&&t.award_list&&t.award_list.length?(t.award_list.forEach(function(e,t){e.activity_start_time=f(e.activity_start_time),e.activity_end_time=f(e.activity_end_time),e.coupon_start_time=f(e.coupon_start_time),e.coupon_end_time=f(e.coupon_end_time)}),e.notEmpty=!0):e.empty=new s.EmptyList({isEmpty:!0,title:"暂无中奖记录"}),t.award_list},errorCallback:function(){}}),e.goUse=function(n,i){l.clickStat(l.EVENT_NAME.BUTTON_CLICK,"red_goToUseCoupon"),"01"==n?t.path("/coupon/list").search({activity_type:"01"}):"03"==n&&(e.creditFlag=c.get(a.CACHE.CREDIT_FLAG),"Y"==e.creditFlag?m.goMesp({cpmmEntryType:"loanApply",cpmmFreeCouponId:i}):"N"==e.creditFlag&&t.path("/red/unverifyTip"))}}]),app.controller("redHomeController",["$scope","$routeParams","$sce","$location","CONSTANT","registerFactory","feSession","feAlert","feAnalyticsService","commonConfig","fePageService","feWebService","apiConfig",function(e,t,n,a,i,o,r,s,c,l,u,d,p){function m(){d.http(angular.extend(y,p.red.getActivityInfo)).then(function(t){e.activityInfo=t.data,e.activityStatus=t.data.activity_status;var n=t.data.start_time,a=(t.data.end_time,t.data.countdown_days),i=t.data.countdown_seconds,o=n.split(" "),r=o[0].split("-");e.start_time={month:"",day:""},e.start_time.month=r[1],e.start_time.day=r[2],f(a,i),u.setPageTitle(t.data.display_name),c.clickStat(c.EVENT_NAME.ELEMENT_SHOW,"red_redHomePageShow"),C&&(g(),C=!1)})}function f(t,n){var a=3600*t*24+n;if("4"==e.activityStatus){if(!(a>0))return;var i=setInterval(function(){e.$apply(function(){if(a<0)m(),i&&(clearInterval(i),i=null);else{var t=a;e.else_time={hour:{first:"",second:"",third:""},minute:{first:"",second:""},seconds:{first:"",second:""}};var n=Math.floor(t/3600);e.else_time.hour.first=Math.floor(n/100),e.else_time.hour.second=Math.floor(n%100/10),e.else_time.hour.third=n%100%10,t%=3600;var o=Math.floor(t/60);e.else_time.minute.first=Math.floor(o/10),e.else_time.minute.second=o%10,t%=60;var r=t%60;e.else_time.seconds.first=Math.floor(r/10),e.else_time.seconds.second=r%10,a-=1}})},1e3)}else"3"==e.activityStatus&&setTimeout(function(){e.$apply(function(){m()})},1e3*(a+5))}function g(){v();var t=e.activityInfo.activity_rule_desc||"";r.set(i.CACHE.RULE_DESC,t),e.ruleInfo={showRules:!1,activity_desc:n.trustAsHtml(t.replace(/\n/gi,"<br>")),show:function(){e.ruleInfo.showRules=!0}},e.prizeInfo={showPrizeInfo:!1,show:function(){var t=r.get(i.CACHE.WE_PARTNER_REGISTER_STATUS);c.clickStat(c.EVENT_NAME.BUTTON_CLICK,"red_redOpen"),t&&"0"!=t?e.activityInfo.remain_num>0?d.http(angular.extend(y,p.red.drawAward)).then(function(t){if(e.prizeInfo.data=t.data,e.prizeInfo.creditFlag=r.get(i.CACHE.CREDIT_FLAG),t.data){e.prizeInfo.goods_name=e.prizeInfo.data.goods_name,e.prizeInfo.data.draw_status=t.data.draw_status.toUpperCase();var n=e.prizeInfo.data.draw_status;t.data.remain_num>=0&&null!=t.data.remain_num&&("S"!=n&&"F1"!=n||(e.activityInfo.remain_num=t.data.remain_num)),"F3"==n?s.show({class:"red-packet",content:"抱歉，本活动目前仅对符合活动规则的用户开放！",btn:{text:"我知道了"}}):"F4"==n?s.show({class:"red-packet",content:"您的抽奖次数已经用完！",btn:{text:"我知道了"}}):"S"==n||"F1"==n||"F2"==n?e.prizeInfo.showPrizeInfo=!0:s.show({class:"red-packet",content:"活动不可用！",btn:{text:"我知道了"}})}else s.show({class:"red-packet",content:"活动不可用！",btn:{text:"我知道了"}})}):s.show({class:"red-packet",content:"您的抽奖次数已经用完！",btn:{text:"我知道了"}}):e.register.showDialog=!0}},e.register=new o.initRegister}function _(){a.path("/red/prizeList")}function v(){var e=r.get(i.CACHE.WE_PARTNER_REGISTER_STATUS);e&&"0"!=e&&d.http(angular.extend({data:{query_credit_flag:"1",scene_id:"red_packet"}},p.red.getUserInfo)).then(function(e){r.set(i.CACHE.CREDIT_FLAG,e.data.credit_flag)})}function h(t,n){var a=r.get(i.CACHE.WE_PARTNER_REGISTER_STATUS);a&&"0"!=a?t&&"function"==typeof t&&t(n):e.register.showDialog=!0}var C=!0,b=t.activityId,y={data:{activity_id:b}};m(),e.goRecordList=function(){h(_)}}]),app.controller("unverifyTipController",["$scope","$routeParams","$sce","$location","commonService","feUI","CONSTANT","feAnalyticsService","registerFactory","feSession","envConfig","feUtils","feWebService","feCookie","commonConfig","apiConfig",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g){function _(){s.clickStat(s.EVENT_NAME.ELEMENT_SHOW,"red_unverifyPageShow");var t=l.get(r.CACHE.RULE_DESC);e.ruleInfo={showRules:!1,activity_desc:n.trustAsHtml(t.replace(/\n/gi,"<br>")),show:function(){e.ruleInfo.showRules=!0}}}function v(){a.path("/red/prizeList")}function h(t,n){var a=l.get(r.CACHE.WE_PARTNER_REGISTER_STATUS);a&&"0"!=a?t&&"function"==typeof t&&t(n):e.register.showDialog=!0}_(),e.goRecordList=function(){h(v)},e.goToApply=function(){s.clickStat(s.EVENT_NAME.ROUTE_CHANGE,"red_goLoanApply_unverifyPage"),i.goMesp({cpmmEntryType:"cpmmEntry"})}}]),app.controller("redLoginController",["$scope","$routeParams","CONSTANT","commonConfig","marketingService","commonService","feUtils","feCache","feCookie","feSession","feWebService","apiConfig","$location",function(e,t,n,a,i,o,r,s,c,l,u,d,p){function m(){var e=c.get(a.cache.cookie.login_token.key),t={data:{query_credit_flag:"1",scene_id:"red_packet"}},i=e?angular.extend(t,d.red.getMgmUserInfo):angular.extend(f,d.red.login);u.http(i).then(function(e){l.set(n.CACHE.WE_PARTNER_REGISTER_STATUS,e.data.we_partner_register_status),l.set(a.cache.session.open_id.key,e.data.open_id),p.path("/red").replace()},function(e){return!e||g>=1?void o.goErrorMsg(e,!1):(g++,void(o.isTokenError(e.status)&&f.code?(c.remove(a.cache.cookie.login_token.key,{path:a.cache.cookie.login_token.path}),m()):o.goErrorMsg(e,!0)))})}var f={data:{code:r.getParamFromURL("code"),scene_id:"red_packet"}},g=0;m()}]),app.controller("redWeixinLoginController",["$scope","$routeParams","apiConfig","feWebService","feUtils","commonConfig","commonService","weixinService",function(e,t,n,a,i,o,r,s){var c=a.getLocalFileAbsUrl(n.red.loginRoute);c=i.appendParamsToRouter(c,t),window.location.href=s.getWeChatOAuthUrl(c)}]),app.controller("applyProcessController",["$scope","$routeParams","$location","feUtils","feWebService","marketingService","apiConfig","weixinService","$sce","feEnvService","envConfig","commonConfig","feUI","feCookie","CONSTANT","feTips","feConfirm","commonService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v){function h(){var e=d.cache.cookie,n=e.mgm_activity_id,a=e.invite_union_id,i=e.agree_register_wepartner,o=!!location.href.startsWith("https");m.set(n.key,t.mgmActivityId,{path:n.path,secure:o}),m.set(a.key,t.inviteUnionId,{path:a.path,secure:o}),m.set(i.key,"Y",{path:i.path,secure:o}),window.location.href=u.mespUrl}t.inviteUnionId&&t.mgmActivityId||v.goErrorMsg({},!0),e.button={applyBtn:new p.Button({text:"开始申请",waValue:"mgm_applyProcess_applyBtn",click:function(){d.cache.cookie;h()}})}}]),app.controller("inviteRegisterController",["$scope","$routeParams","$location","feUtils","feWebService","marketingService","apiConfig","weixinService","$sce","feEnvService","envConfig","commonConfig","feUI","feCookie","CONSTANT","feTips","feConfirm","commonService","feAnalyticsService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g,_,v,h){function C(){n.path("/mgm/applyProcess").search({mgmActivityId:t.mgmActivityId,inviteUnionId:t.inviteUnionId})}e.showPage=!1,t.inviteUnionId&&t.mgmActivityId||v.goErrorMsg({},!0),h.bpButtonClick("from_invite_register_link");var b=a.getParamFromURL("code");if(!b)return void(window.location.href=s.getWeChatOAuthUrl(window.location.href));var y={data:{code:a.getParamFromURL("code"),client_sys_info:a.getClientSysInfo()}};o.login(y,{activity_type:"01"},function(n){var a={data:{activity_id:t.mgmActivityId,aes:"Y"}};i.http(angular.extend(a,r.activity.getActivityInfo)).then(function(t){e.showPage=!0;var n=t.data;
// e.activityInfo=n,e.isEnd=n.is_end==f.APP.ACTIVITY_IS_END.IS_END,e.isClosed=n.activity_status==f.APP.MGM_ACTIVITY_STATUS.CLOSED,e.activityInfo.activity_rule_desc=c.trustAsHtml(n.activity_rule_desc.replace(/\\n/g,"<br>")),e.showRulesDialog=function(){e.activityInfo.showRules=!0}},function(e){v.goErrorMsg(e,!0)})}),e.button={agreeInvitedBtn:new p.Button({text:"接受邀请",waValue:"mgm_inviteRegister_agreeInvitedBtn",click:function(){d.cache.cookie;C()}}),goRegister:new p.Button({text:"推荐有礼",waValue:"mgm_inviteRegister_goRegister",click:function(){n.path("/marketing/login").search({activity_type:"01"})}})}}]),app.controller("registerController",["$scope","mgmUI","feUI","$location","feUtils","feCookie","feAlert","$routeParams","$sce","marketingService","weixinService","feAnalyticsService","commonConfig","apiConfig","feWebService","commonService",function(e,t,n,a,i,o,r,s,c,l,u,d,p,m,f,g){function _(){var t=e.register.phoneNo;return i.isValidPhoneNumber(t)?(e.register.otp.countdown(" s","发送验证码"),void f.http(angular.extend({data:{mobileNo:t}},m.register.getMgmOtp)).then(function(e){})):void(e.register.errMsg=p.msg.mobileNumError)}function v(){e.register.errMsg="";var t=e.register.phoneNo,n=e.register.authCode;if(!i.isValidPhoneNumber(t))return void(e.register.errMsg=p.msg.mobileNumError);if(!n)return e.register.errMsg=p.msg.otpCodeNULLError,void(e.register.submitBtnDisabled=!0);var a={data:{phone_no:t,otp_code:n}};f.http(angular.extend(a,m.register.mgmRegister)).then(function(t){var n=t.data;0==n.status?h():1==n.status?e.register.errMsg=p.msg.sysBusyError:2==n.status?e.register.errMsg=p.msg.mobileAreadyRegister:3==n.status?e.register.errMsg=p.msg.authCodeErr:e.register.errMsg=p.msg.sysBusyError})}function h(){var e=o.get(p.cache.cookie.login_token.key);if(e){var t=s.activity_type;l.login({},{activity_type:t})}else r.show({content:p.msg.tokenOutTime,btn:{text:"确定",click:function(){u.invoke("closeWindow")}}})}e.register=new t.MgmRegister({showDialog:!1,submitBtnDisabled:!0,otp:{text:"发送验证码",waValue:"mgm_register_otpBtn",click:function(){return!this.disabled()&&void _()}},submit:function(){d.bpButtonClick("mgm_register_submitBtn"),v()}}),e.activityInfo={};var C={data:{code:i.getParamFromURL("code"),client_sys_info:i.getClientSysInfo()}};l.login(C,{activity_type:"01"},function(t){var n=t.mgm_activity?t.mgm_activity:t.current_activity,a=n.activity_rule_desc||"";e.showRulesDialog=function(){e.activityInfo.showRules=!0},e.activityInfo.activity_rule_desc=c.trustAsHtml(a.replace(/\\n/g,"<br>"))}),e.joinWeBanBtn=function(){d.bpButtonClick("mgm_register_joinWeBanBtn"),e.register.showDialog=!0}}]),app.controller("telemarketing",["$scope","feWebService","apiConfig","$routeParams","envConfig",function(e,t,n,a,i){var o=i.cdnPrefix,r={data:{srcId:a.srcId}};t.http(angular.extend(r,n.telemarketing.qrySourceIdFromMsg)).then(function(t){t&&t.data&&(e.imgUrl=o+t.data.cdnPath)})}]);
// //# sourceMappingURL=../../../rev/app.js.map
