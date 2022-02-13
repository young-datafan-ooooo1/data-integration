/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

/**
 *
 */

package com.youngdatafan.di.run.management.server.function;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.xml.XMLHandler;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author matt
 *
 *
 *         <pre>
 *
 *    <function>
 *   <category>Text</category>
 *   <name>&amp;</name>
 *   <description>Concatenate two strings.</description>
 *   <syntax>Text Left &amp; Text Right</syntax>
 *   <returns>Text</returns>
 *   <constraints>None</constraints>
 *   <semantics>Concatenates two text (string) values. Due to the way conversion works, numbers are converted to
 *   strings. Note that this is equivalent to CONCATENATE(Left,Right).
 *   (Note: CONCATENATE is not yet available in libformula version 0.1.18.2)</semantics>
 *   <examples>
 *   <example><expression>"Hi " &amp; "there"</expression> <result>"Hi there"</result>
 *     <level>1</level> <comment>Simple concatenation.</comment></example>
 *  <example><expression>"H" &amp; ""</expression> <result>"H"</result>
 *    <level>1</level> <comment>Concatenating an empty string produces no change.</comment></example>
 *  <example><expression>-5&amp;"b"</expression> <result>-5b</result>
 *    <level>1</level> <comment>Unary - has higher precedence than &amp;</comment></example>
 *  <example><expression>3&amp;2-1</expression> <result>31</result>
 *    <level>1</level> <comment>Binary - has higher precedence than &amp;</comment></example>
 *   </examples>
 *   </function>
 * </pre>
 */
public class FunctionDescription {
    public static final String XML_TAG = "function";


    public static final String FUN_CASE = "case";
    public static final String FUN_AND = "and";
    public static final String FUN_OR = "or";
    public static final String FUN_NOT = "not";
    public static final String FUN_ABS = "abs";
    public static final String FUN_ROUND = "round";
    public static final String FUN_FLOOR = "floor";
    public static final String FUN_CEIL = "ceil";
    public static final String FUN_CONNECT = "||";
    public static final String FUN_FIND = "find";
    public static final String FUN_LOWER = "lower";
    public static final String FUN_UPPER = "upper";
    public static final String FUN_TRIM = "trim";
    public static final String FUN_LTRIM = "ltrim";
    public static final String FUN_RTRIM = "rtrim";
    public static final String FUN_LEFT = "left";
    public static final String FUN_RIGHT = "right";
    public static final String FUN_MID = "mid";
    public static final String FUN_SUBSTITUTE = "substitute";
    public static final String FUN_DATE_BETWEEN = "date_between";
    public static final String FUN_LAST_DATE_YEAR="last_date_year";
    public static final String FUN_LAST_DATE_QUARTER="last_date_quarter";
    public static final String FUN_LAST_DATE_MONTH="last_date_month";
    public static final String FUN_LAST_DATE_WEEK="last_date_week";
    public static final String FUN_FIRST_DATE_YEAR="first_date_year";
    public static final String FUN_FIRST_DATE_QUARTER="first_date_quarter";
    public static final String FUN_FIRST_DATE_MONTH="first_date_month";
    public static final String FUN_FIRST_DATE_WEEK="first_date_week";
    public static final String FUN_TO_CHAR = "to_char";
    public static final String FUN_TO_NUMBER = "to_number";
    public static final String FUN_TO_DATE = "to_date";
    public static final String FUN_IS_NULL = "is_null";


    private String category;
    private String name;
    private String ckName;
    private String description;
    private String syntax;
    private String returns;
    private String constraints;
    private String semantics;
    private List<FunctionExample> functionExamples;

    /**
     * @param category
     * @param name
     * @param description
     * @param syntax
     * @param returns
     * @param constraints
     * @param semantics
     * @param functionExamples
     */
    public FunctionDescription(String category, String name, String ckName, String description, String syntax, String returns,
                               String constraints, String semantics, List<FunctionExample> functionExamples) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.returns = returns;
        this.constraints = constraints;
        this.semantics = semantics;
        this.functionExamples = functionExamples;
        this.ckName = ckName;
    }

    public FunctionDescription(Node node) {
        this.category = XMLHandler.getTagValue(node, "category");
        this.name = XMLHandler.getTagValue(node, "name");
        this.description = XMLHandler.getTagValue(node, "description");
        this.syntax = XMLHandler.getTagValue(node, "syntax");
        this.returns = XMLHandler.getTagValue(node, "returns");
        this.constraints = XMLHandler.getTagValue(node, "constraints");
        this.semantics = XMLHandler.getTagValue(node, "semantics");
        this.ckName = XMLHandler.getTagValue(node, "ckName");
        this.functionExamples = new ArrayList<FunctionExample>();

        Node examplesNode = XMLHandler.getSubNode(node, "examples");
        int nrExamples = XMLHandler.countNodes(examplesNode, FunctionExample.XML_TAG);
        for (int i = 0; i < nrExamples; i++) {
            Node exampleNode = XMLHandler.getSubNodeByNr(examplesNode, FunctionExample.XML_TAG, i);
            this.functionExamples.add(new FunctionExample(exampleNode));
        }
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *          the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *          the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *          the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the syntax
     */
    public String getSyntax() {
        return syntax;
    }

    /**
     * @param syntax
     *          the syntax to set
     */
    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    /**
     * @return the returns
     */
    public String getReturns() {
        return returns;
    }

    /**
     * @param returns
     *          the returns to set
     */
    public void setReturns(String returns) {
        this.returns = returns;
    }

    /**
     * @return the constraints
     */
    public String getConstraints() {
        return constraints;
    }

    /**
     * @param constraints
     *          the constraints to set
     */
    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    /**
     * @return the semantics
     */
    public String getSemantics() {
        return semantics;
    }

    /**
     * @param semantics
     *          the semantics to set
     */
    public void setSemantics(String semantics) {
        this.semantics = semantics;
    }

    /**
     * @return the functionExamples
     */
    public List<FunctionExample> getFunctionExamples() {
        return functionExamples;
    }

    /**
     * @param functionExamples
     *          the functionExamples to set
     */
    public void setFunctionExamples(List<FunctionExample> functionExamples) {
        this.functionExamples = functionExamples;
    }

    /**
     * Create a text version of a report on this function
     *
     * @return
     */
    public String getHtmlReport() {
        StringBuilder report = new StringBuilder(200);

        // The function name on top
        //
        report.append("<H2>").append(name).append("</H2>").append(Const.CR);

        // Then the description
        //
        report.append("<b><u>描述:</u></b> ").append(description).append("<br>").append(Const.CR);

        // Syntax
        //
        if (!Utils.isEmpty(syntax)) {
            report.append("<b><u>语法:</u></b> <pre>").append(syntax).append("</pre><br>").append(Const.CR);
        }

        // Returns
        //
        if (!Utils.isEmpty(returns)) {
            report.append("<b><u>返回值:</u></b>  ").append(returns).append("<br>").append(Const.CR);
        }

        // Constraints
        //
        if (!Utils.isEmpty(constraints)) {
            report.append("<b><u>约束:</u></b>  ").append(constraints).append("<br>").append(Const.CR);
        }

        // Semantics
        //
        if (!Utils.isEmpty(semantics)) {
            report.append("<b><u>含义:</u></b>  ").append(semantics).append("<br>").append(Const.CR);
        }

        // Examples
        //
        if (functionExamples.size() > 0) {
            report.append(Const.CR);
            report.append("<br><b><u>样例:</u></b><p>  ").append(Const.CR);

            report.append("<table border=\"1\">");

            report.append("<tr>");
            report.append("<th>表达式</th>");
            report.append("<th>返回值</th>");
            report.append("<th>注释</th>");
            report.append("</tr>");

            for (FunctionExample example : functionExamples) {
                // <example><expression>"Hi " &amp; "there"</expression> <result>"Hi there"</result> <level>1</level>
                // <comment>Simple concatenation.</comment></example>

                report.append("<tr>");
                report.append("<td>").append(example.getExpression()).append("</td>");
                report.append("<td>").append(example.getResult()).append("</td>");
                if (!Utils.isEmpty(example.getComment())) {
                    report.append("<td>").append(example.getComment()).append("</td>");
                }
                report.append("</tr>");
                report.append(Const.CR);
            }
            report.append("</table>");
        }

        return report.toString();
    }

    public String getCkName() {
        return ckName;
    }

    public void setCkName(String ckName) {
        this.ckName = ckName;
    }
}
