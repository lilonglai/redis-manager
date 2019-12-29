<template>
  <div id="system-operation" class="body-wrapper">
    <el-row>
      <el-col :xl="24" :lg="24" :md="24" :sm="24">
        <div class="console-wrapper">
          <div class="console-title">System Command</div>
          <div class="console">
            <div class="command-history-wrapper">
              <div class="history" v-html="histrory"></div>
            </div>
            <div class="command">
              <div style="max-width: 50%; margin-right: 5px;"></div>
              <el-input
                size="small"
                type="text"
                class="command-input"
                v-model="command"
                @keyup.enter.native="keyUpEnter(command)"
              >
                <template slot="prepend">$</template>
              </el-input>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { store } from "@/vuex/store.js";
import API from "@/api/api.js";
import { isEmpty } from "@/utils/validate.js";
import { codemirror } from "vue-codemirror-lite";
import message from "@/utils/message.js";
export default {
  components: {
    codemirror
  },
  data() {
    return {
      histrory: "",
      command: ""
    };
  },
  methods: {
    sendCommand(dataCommandsParam) {
      let url = "/data/executeCommand";
      API.post(
        url,
        dataCommandsParam,
        response => {
          let result = response.data;
          if (result.code == 0) {
            let data = result.data;
            if (typeof data != "string") {
              data = JSON.stringify(data);
            }
            this.histrory +=
              "$ " +
              this.command +
              "<br/>" +
              data +
              "<br/>";
          } else {
            this.histrory +=
              "$ " +
              this.command +
              "\n no data or operation failed or command not support \n";
          }
          this.command = "";
        },
        err => {
          message.error(err);
        }
      );
    },
    keyUpEnter() {
      if (isEmpty(this.command)) {
        return;
      }
      if (this.command == "clear") {
        this.histrory = "";
        this.command = "";
        return;
      }
      let dataCommandsParam = {
        command: this.command
      };
      this.sendCommand(dataCommandsParam);
    },
    reset() {
      this.histrory = "";
      this.command = "";
    }
  },
  computed: {
    currentGroup() {
      return store.getters.getCurrentGroup;
    }
  },
  watch: {
    currentGroup(group) {
      this.reset()
      this.$router.push({
        name: "system-operation",
        params: { groupId: group.groupId }
      });
    }
  },
  mounted() {
  }
};
</script>

<style scoped>
.body-wrapper {
  padding: 20;
}

.select-wrapper {
  margin-bottom: 20px;
}

.console-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.console {
  min-height: 600px;
  padding: 10px 0;
  background-color: black;
  color: #ffffff;

  font-family: Consolas, Monaco, Menlo, "Courier New", monospace !important;
  margin: 0;
}
.console-title {
  padding: 10px 20px;
  border-bottom: 1px solid #dcdfe6;
  background: #f0f2f5;
}
.command-history-wrapper {
  word-break: break-all;
  word-wrap: break-word;
}

.command-input >>> .el-input__inner,
.command-input >>> .el-input-group__prepend {
  font-family: Consolas, Monaco, Menlo, "Courier New", monospace !important;
  border: none !important;
  font-size: 14px;
  background-color: #000000 !important;
  color: #ffffff !important;
  padding: 0;
}

.command-input >>> .el-input-group__prepend {
  padding-right: 5px;
}

.command-input >>> .el-input__inner:focus {
  outline: none;
}
.command-input >>> .el-input__inner ::-webkit-input-placeholder {
  caret-color: red;
}

.history {
  width: auto;
  line-height: 20px;
  word-break: normal;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: Consolas, Monaco, Menlo, "Courier New", monospace !important;
  line-height: 24px;
}

.result-wrapper {
  height: auto;
  padding: 0;
  line-height: 20px;
  word-break: normal;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.cm-s-seti.CodeMirror {
  background: #000000 !important;
}
.cm-s-seti .CodeMirror-activeline-background {
  background: #000000;
}
.cm-s-seti .CodeMirror-selected {
  background: #d7d4f0;
}
.cm-s-seti .CodeMirror-linenumber {
  color: #aeaeae;
} /*行号数字*/
.cm-s-seti .cm-quote {
  color: #090;
} /*引号*/
.cm-s-seti .cm-keyword {
  color: #3300cc;
} /*关键字，例如：SELECT*/
.cm-s-seti .cm-number {
  color: #333333;
} /*数字*/
.cm-s-seti .cm-variable-2 {
  color: #333333;
} /*变量2，例如：a.id中的.id*/
.cm-s-seti .cm-comment {
  color: #009933;
} /*注释*/
.cm-s-seti .cm-string {
  color: #009933;
} /*字符串*/
.cm-s-seti .cm-string-2 {
  color: #009933;
} /*字符串*/
</style>