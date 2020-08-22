/**
 * Encryption/decryption
 */
aesUtil = {

    // get key
    genKey : function (length = 16) {
        let random = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        let str = "";
        for (let i = 0; i < length; i++) {
            str  = str + random.charAt(Math.random() * random.length)
        }
        return str;
    },

    // Encrypt
    encrypt : function (plaintext,key) {
        if (plaintext instanceof Object) {
            //JSON.stringify
            plaintext = JSON.stringify(plaintext)
        }
        let encrypted = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(plaintext), CryptoJS.enc.Utf8.parse(key), {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
        return encrypted.toString();
    },

    // Decrypt
    decrypt : function (ciphertext,key) {
        let decrypt = CryptoJS.AES.decrypt(ciphertext, CryptoJS.enc.Utf8.parse(key), {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
        let decString = CryptoJS.enc.Utf8.stringify(decrypt).toString();
        if(decString.charAt(0) === "{" || decString.charAt(0) === "[" ){
            //JSON.parse
            decString = JSON.parse(decString);
        }
        return decString;
    }
};
rsaUtil = {
    //RSA bits
    bits: 1024,

    // current JSEncrypted object
    thisKeyPair: {},

    // Generate key pair
    genKeyPair: function (bits = rsaUtil.bits) {
        let genKeyPair = {};
        rsaUtil.thisKeyPair = new JSEncrypt({default_key_size: bits});

        // get private key
        genKeyPair.privateKey = rsaUtil.thisKeyPair.getPrivateKey();

        // get public key
        genKeyPair.publicKey = rsaUtil.thisKeyPair.getPublicKey();

        return genKeyPair;
    },

    // public key encryption
    encrypt: function (plaintext, publicKey) {
        if (plaintext instanceof Object) {
            //1、JSON.stringify
            plaintext = JSON.stringify(plaintext)
        }
        publicKey && rsaUtil.thisKeyPair.setPublicKey(publicKey);
        return rsaUtil.thisKeyPair.encrypt(JSON.stringify(plaintext));
    },

    // private key decryption
    decrypt: function (ciphertext, privateKey) {
        privateKey && rsaUtil.thisKeyPair.setPrivateKey(privateKey);
        let decString = rsaUtil.thisKeyPair.decrypt(ciphertext);
        if(decString.charAt(0) === "{" || decString.charAt(0) === "[" ){
            //JSON.parse
            decString = JSON.parse(decString);
        }
        return decString;
    }
};

/**
 * utils
 */
commonUtil = {
    /**
     * jquery extend
     */
    jqueryFnExtend : function(){
        /**
         * conver to json
         */
        $.fn.serializeObject = function () {
            var o = {};
            var a = this.serializeArray();
            $.each(a, function () {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        };


        $.fn.form = function (data) {
            let form = $(this);
            for (let i in data) {
                let name = i;
                let value = data[i];
                if (name !== "" && value !== "") {
                    valuAtion(name, value);
                }
            }

            function valuAtion(name, value) {
                if (form.length < 1) {
                    return;
                }
                if (form.find("[name='" + name + "']").length < 1) {
                    return;
                }
                let input = form.find("[name='" + name + "']")[0];
                if ($.inArray(input.type, ["text", "password", "hidden", "select-one", "textarea"]) > -1) {
                    $(input).val(value);
                } else if (input.type === "radio" || input.type === "checkbox") {
                    form.find("[name='" + name + "'][value='" + value + "']").attr("checked", true);
                }
            }
        };


        $.delete = function (url, params, callback) {
            if (!params || typeof params === 'string') {
                throw new Error('Error Params：' + params);
            }

            $.ajax({
                url: url,
                type: "DELETE",
                contentType: 'application/json',
                data: JSON.stringify(params),
                success: function (result) {
                    callback && callback(result);
                }
            });
        };
    },


    getNowTime: function () {
        var time = new Date();
        var year = time.getFullYear();
        var month = time.getMonth() + 1;
        var day = time.getDate();

        var hour = time.getHours();
        var minu = time.getMinutes();
        var second = time.getSeconds();
        var data = year + "-";
        if (month < 10) {
            data += "0";
        }
        data += month + "-";
        if (day < 10) {
            data += "0"
        }
        data += day + " ";
        if (hour < 10) {
            data += "0"
        }


        data += hour + ":";
        if (minu < 10) {
            data += "0"
        }
        data += minu + ":";
        if (second < 10) {
            data += "0"
        }
        data += second;
        return data;
    },


    updateKeyForLayuiTree: function (arrar) {
        let newArray = [];
        for (let i = 0; i < arrar.length; i++) {
            let obj1 = {};
            let obj = arrar[i];
            obj1.id = obj.menuId;
            obj1.title = obj.menuName;
            obj1.href = obj.menuPath;

            if (obj.children.length > 0) {
                obj1.children = this.updateKeyForLayuiTree(obj.children);
            }
            newArray.push(obj1);
        }
        return newArray
    },


    checkedForLayuiTree:function (arrTree, userTreeString) {
        for(let tree of arrTree){

            tree.spread=true;

            if(tree.children && tree.children.length > 0){
                tree.children = this.checkedForLayuiTree(tree.children,userTreeString);
            }else{

                if(userTreeString.search(tree.id) !== -1){
                    tree.checked = true;
                }
            }
        }
        return arrTree;
    }
};




commonUtil.jqueryFnExtend();

// api on or off
if(sessionStorage.getItem('sysApiEncrypt') === "Y"){
    // get frontend rsa public key , aes key to the window
    let genKeyPair = rsaUtil.genKeyPair();
    window.jsPublicKey = genKeyPair.publicKey;
    window.jsPrivateKey = genKeyPair.privateKey;

    let _ajax = $.ajax;
    $.ajax = function (opt) {

        let fn = {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            success: function (data, textStatus) {
            }
        };
        if (opt.error) {
            fn.error = opt.error;
        }
        if (opt.success) {
            fn.success = opt.success;
        }

        // encrypt and transport
        if (opt.type.toLowerCase() === "post") {
            let data = opt.data;
            // Get AES key randomly before sending request
            let aesKey = aesUtil.genKey();
            data = {
                data: aesUtil.encrypt(data, aesKey),// Data after AES encryption
                aesKey: rsaUtil.encrypt(aesKey, sessionStorage.getItem('javaPublicKey')),//AES Key after backend RSA public key encryption
                publicKey: window.jsPublicKey// Front end public key
            };
            opt.data = data;
        }


        let _opt = $.extend(opt, {
            success: function (data, textStatus) {
                if (opt.type.toLowerCase() === "post") {
                    data = aesUtil.decrypt(data.data.data, rsaUtil.decrypt(data.data.aesKey, window.jsPrivateKey));
                }
                // Get plaintext aes key, then use AES key to decrypt
                fn.success(data, textStatus);
            }
        });
        return _ajax(_opt);
    };
}



