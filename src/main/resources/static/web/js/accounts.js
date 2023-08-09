Vue.createApp({
    data() {
        return {
            clientInfo: {},
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData() {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get(`/api/clients/${id}`)
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-us');
        },
    },
    mounted() {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app');