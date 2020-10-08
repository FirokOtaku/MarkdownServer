const {log} = console;

function getUrlParam(name)
{
    let reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);

    if(r!=null)
        return unescape(r[2]);
    return null;
}

log('开始初始化')

// Vue.use(VueRouter);

const app = new Vue({
    el: '#app',
    data: {
        dir: {
            path: '',
            childrenDir: [],
            childrenFile: [],
        },
        pages: [
            {
                title: 'page1',
                type: 'doc',
                content: '# test',
            },
            {
                title: 'page2',
                type: 'create',
                path: 'test/test',
                content: 'tesswqaswadsaw',
            },
        ]
    },
    methods: {
        clickDir: function(path)
        {
            let dir2 = [];
            for(let d of this.dir.paths) dir2.push(d);
            dir2.push(path);
            this.switchDir(dir2);
        },
        clickFile: function (path)
        {
            log('打开文件:'+this.dir.path + path);
        },

        switchDir: function (paths=[])
        {
            axios.post('/api/dir/list',{
                paths: paths
            })
            .then(res=>{
                this.dir = res.data.data;
                // log(res.data);
            })
            .catch(err=>{
                log('切换文件夹时发生错误');
                log(err);
            });
        },
        switchParent: function ()
        {
            let dir2 = [];
            for(let i=0;i<this.dir.paths.length-1;i++)
            {
                dir2.push(this.dir.paths[i]);
            }
            this.switchDir(dir2);
        },
    },
});

let paramPaths = getUrlParam('paths');
app.switchDir(paramPaths!=null?paramPaths.split(','):[]);

log('初始化完成')
