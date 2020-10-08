const {log} = console;

Vue.component('button-counter', {
    data: function () {
        return {
            count: 0
        }
    },
    template:
`
<button v-on:click="count++">
You clicked me {{ count }} times.
</button>
`
});


Vue.component('md-viewer',{
    props: ['md'],
    computed: {
        html: function(){
            return marked(this.md);
        }
    },
    template:
`
<div v-html="html"></div>
`
});
Vue.component('md-editor',{
    data: function(){
        return {
            md: '',
        };
    },
    methods: {
        clickCancel: function () {
            this.$emit('click-cancel',this.md);
        },
        clickSave: function () {
            this.$emit('click-save',this.md);
        }
    },
    computed: {
        html: function (){
            return marked(this.md);

        }
    },
    template:
`
<div>
    <textarea v-model="md"></textarea>
    <button class="button-small" @click="clickSave">Save</button>
    <button class="button-small" @click="clickCancel">Cancel</button>
    <div v-html="html"></div>
</div>
`
})

// Vue Router 组件路由
// const MdViewer = Vue.component('md-viewer');
// const MdEditor = Vue.component('md-editor');

const routes = [
    // { path: '/view', component: MdViewer },
    // { path: '/edit', component: MdEditor },
]
const router = new VueRouter({ routes });

// 调整marked图片渲染器
marked.Renderer.prototype.image = (href,title,text)=>{
    // log('渲染图片');
    if(!href.startsWith('http'))
    {
        // log('渲染到一张非网络图片');
        // log(href);
        // log(title);
        // log(text);
        let paths = [];
        for(let p of href.split(/[\/|\\]/)) if(p.length>=0) paths.push(p);
        let tempUrl = '/api/img/get?';
        for(let i=0;i<paths.length-1;i++)
        {
            let p = paths[i];
            tempUrl += 'paths='+p+'&';
        }
        tempUrl += 'file='+ paths[paths.length-1];
        href = tempUrl;
    }
    return `<img src="${href}" title="${title}" alt="${text}" style="max-width:100%">`;
}
marked.setOptions({
    highlight: (code)=>hljs.highlightAuto(code).value,
});
const appIndexMd =
`
# Markdown 文档查看器

`;

const app = new Vue({
    el: '#app',
    router: router,
    data: {
        dir: {
            paths: [],
            childrenDir: [],
            childrenFile: [],
        },
        file: {
            paths: [],
            name: undefined,
            content: appIndexMd,
            changed: false,
            method: 'view',
        },

        windowMethod: 'both'
    },
    methods: {
        clickParentDir: function() {
            let pathsNew = this.dir.paths.slice(0,this.dir.paths.length-1);
            this.pushPath(pathsNew);
        },
        clickChildDir: function(path){
            if(path===undefined||path===null) return;
            let pathsNew = this.dir.paths.splice(0,this.dir.paths.length);
            pathsNew.push(path);
            this.pushPath(pathsNew);
        },
        clickFile: function(file){
            this.pushPath(this.dir.paths,file);
            this.file.method = 'view';
        },

        syncFile: function() // 同步当前文件内容到服务器
        {
            if(!this.file.changed) return; // 没有改动直接返回

            this.file.changed = false;
        },
        deleteFile: function() // 删除当前文件
        {
            this.file.changed = false;
        },

        changeMethod: function(method='view')
        {
            this.file.method = method;
        },

        pushPath: function(paths=[],file=undefined)
        {
            log('请求');
            log(paths);
            log(file);
            let query = {
                path: '',
                query: {
                    'paths': paths!=undefined && paths.length ? paths : undefined,
                    'file': file,
                },
            };
            this.$router.push(query);
        },


        flushPath: function ()
        {
            const path = this.path;

            const pathTemp = [];
            for(let p of path.paths!=null?path.paths:[])
            {
                if(p===undefined||p===null||p.trim().length<=0) continue;
                pathTemp.push(p);
            }

            this.dir.paths = pathTemp;

            // 刷新目录数据
            axios.post('/api/dir/list',{
                'paths': path.paths,
            })
            .then((response)=>{
                this.dir = response.data.data;
            })
            .catch((error)=>{
                log('请求文件夹数据时发生错误');
                log(error);
            });

            // 刷新文件数据
            if(path.file!=undefined)
            {
                const pathsTemp = path.paths && path.paths.length? path.paths : undefined;
                const fileTemp = path.file;

                axios.get('/api/doc/file',{
                    params: {
                        paths: pathsTemp,
                        file: fileTemp,
                    },
                    paramsSerializer: params => {
                        return Qs.stringify(params, { indices: false })
                    },
                })
                .then((response)=>{
                    // log('文件数据');
                    // log(response);

                    this.file.paths = pathTemp;
                    this.file.name = fileTemp;
                    this.file.content = response.data;
                })
                .catch((error)=>{
                    log('请求文件数据时发生错误');
                    log(error);
                });
            }
        },
    },
    computed: {
        path: function()
        {
            let data = this.$route.query;
            if(data.paths==undefined) data.paths=[];
            return data;
        },

        viewMethod: function()
        {
            /*
            view - 查看模式
            both - 对照模式
            edit - 编辑模式
            * */
            return this.file.method;
        },
        htmlContent: function()
        {
            return marked(this.file.content);
        },
    },
});


router.afterEach((to, from) => {
    log('路由改变'+new Date());
    log(to);
    app.flushPath();
})

app.flushPath();

