package com.youngdatafan.di.run.management.steps.regex.controller;


import com.youngdatafan.dataintegration.core.model.Result;
import com.youngdatafan.dataintegration.core.util.StatusCode;
import com.youngdatafan.di.run.management.steps.regex.api.RegexServiceApi;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/regex")
public class RegexController implements RegexServiceApi {
    @Override
    public Result ismatch(String regex, String matcherValue) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(matcherValue);
        boolean ismatch = m.matches();
        return Result.success(ismatch);
    }

    @Override
    public Result<List<String>, Object> groupCount(String regex, String matcherValue) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(matcherValue);
        boolean ismatch = m.matches();
        if (!ismatch) {
            return Result.fail(StatusCode.CODE_10000.getCode(), ismatch, "正则匹配错误，请重新修改 " + regex);
        }
        int nrFields = m.groupCount();
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= nrFields; i++) {
            if (m.group(i) == null) {
                list.add("");
            } else {
                list.add(m.group(i));
            }

        }
        return Result.success(list);
    }
}
