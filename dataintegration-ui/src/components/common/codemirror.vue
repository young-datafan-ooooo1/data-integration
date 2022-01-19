<template>
  <div style="border: 1px solid #B3C0D1; border-radius: 5px;">
    <textarea ref="mycode" id="myCode" class="codesql" v-model="code"></textarea>
  </div>

</template>

<script>
  import "codemirror/theme/ambiance.css";
  import "codemirror/lib/codemirror.css";
  import "codemirror/addon/hint/show-hint.css";
  import 'codemirror/mode/javascript/javascript.js'

  let CodeMirror = require("codemirror/lib/codemirror");
  require("codemirror/addon/edit/matchbrackets");
  require("codemirror/addon/selection/active-line");
  require("codemirror/mode/sql/sql");
  require("codemirror/addon/hint/show-hint");
  require("codemirror/addon/hint/sql-hint");
  export default {
    name: "codeMirror",
    props:{
      mime:String,
    },
    data() {
      return {
        code: '',
        editor: '',
        // mime:'text/x-mariadb',
      }
    },
    mounted() {
      // this.mime = ''
      console.info("-=mime-=",this.mime);
      //let theme = 'ambiance'//设置主题，不设置的会使用默认主题
      let editor = CodeMirror.fromTextArea(this.$refs.mycode, {
        mode: this.mime, //选择对应代码编辑器的语言
        indentWithTabs: true,
        smartIndent: true,
        lineNumbers: true,
        matchBrackets: true,
        //theme: theme,
        // autofocus: true,
        // extraKeys: {'Ctrl': 'autocomplete'},//自定义快捷键
        hintOptions: { //自定义提示选项
          tables: {
            users: ['name', 'score', 'birthDate'],
            countries: ['name', 'population', 'size']
          }
        }
      })

      this.editor = editor;
    },
    methods: {
      submit() {
        console.info(this.editor.getValue());
      },
      setValue(params){
        this.editor.setValue(params);
      },
    }
  }
</script>

<style>
  .codesql {
    height: 100px;
    width: 600px;
    font-size: 11pt;
    font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, serif;
  }
  .CodeMirror {
      font-family: monospace;
      height: 150px;
  }
</style>
