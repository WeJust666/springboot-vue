import VueRouter from 'vue-router';

const routes = [
    {
        path: '/',
        name:'login',
        component: () => import('../components/Login.vue')
    },
    {
        path: '/Index',
        name:'index',
        component: () => import('../components/Index.vue'),
        children: [
            {
                path:'/Home',
                name:'home',
                meta:{
                    title: '首页'
                },
                component: () => import('../components/Home.vue')
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

//重置路由
export function resetRouter() {
    router.matcher = new VueRouter({
        mode: 'history',
        routes: []
    }).matcher
}

//解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

export default router;