<template>
  <div id="textEdit" class="edit" contenteditable="true">

  </div>
</template>

<script>
  export default {
    name: "textEdit",
    data() {
      return {
        data: '1232145'
      }
    },
    mounted() {
      this.init();
    },
    methods: {
      init() {
        var text = document.createElement('textarea');
        text.style.display = 'none';
        text.setAttribute("id", "text");
        // text.setAttribute("v-model","data");
        var body = document.getElementById('textEdit');
        $('#textEdit').bind('input', function() {
          let val = $('#textEdit').text();
          let val1 = '';
          if (val.indexOf('select') >= 0) {
            val1 = val.replace(/select/g, '<span>你号</span>')
            $("#textEdit").append(val1);
          }
          // $('#textEdit').html($(this).val().length + ' characters');
        });
      },
      insertHtmlAtCaret(childElement) {
        var sel, range;
        if (window.getSelection) {
          // IE9 and non-IE
          sel = window.getSelection();
          if (sel.getRangeAt && sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();

            var el = document.createElement("div");
            el.appendChild(childElement);
            var frag = document.createDocumentFragment(),
              node, lastNode;
            while ((node = el.firstChild)) {
              lastNode = frag.appendChild(node);
            }

            range.insertNode(frag);
            if (lastNode) {
              range = range.cloneRange();
              range.setStartAfter(lastNode);
              range.collapse(true);
              sel.removeAllRanges();
              sel.addRange(range);
            }
          }
        } else if (document.selection && document.selection.type != "Control") {
          // IE < 9
          document.selection.createRange().pasteHTML(html);
        }
      }
    }
  }
</script>

<style>
  .edit {
    width: 400px;
    height: 300px;
    border: 1px solid #99A9BF;
  }


  .intro {
    color: red;
  }
</style>
