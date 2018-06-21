
var h5Standard = {
    /**
     * [previewImage H5预览图片]
     * @return {[type]} [description]
     */
    previewImage : function(file, prvid) {
        /* file：file控件
         * prvid: 图片预览容器
         */
        var tip = "Expect jpg or png or gif!"; // 设定提示信息
        var filters = {
            "jpeg"  : "/9j/4",
            "gif"   : "R0lGOD",
            "png"   : "iVBORw"
        };
        var prvbox = $('#'+prvid);
        if (window.FileReader) { // html5方案
            for (var i=0, f; f = file.files[i]; i++) {
                var fr = new FileReader();
                fr.onload = function(e) {
                    var src = e.target.result;
                    if (!validateImg(src)) {
                        alert(tip)
                    } else {
                        showPrvImg(src);
                    }
                }
                fr.readAsDataURL(f);
            }
        } else { // 降级处理
            if ( !/\.jpg$|\.png$|\.gif$/i.test(file.value) ) {
                alert(tip);
            } else {
                showPrvImg(file.value);
            }
        }

        function validateImg(data) {
            var pos = data.indexOf(",") + 1;
            for (var e in filters) {
                if (data.indexOf(filters[e]) === pos) {
                    return e;
                }
            }
            return null;
        }

        function showPrvImg(src) {
            // console.log(src);
            // nativeBridgeCallback['photosCallback'](src);
            document.querySelector("#img").src = src;
        }
    },
    /**
     * [playSelectedFile H5视频预览]
     * @return {[type]} [description]
     */
    playSelectedFile : function(event) {
        var URL = window.URL || window.webkitURL,
            file = this.files[0],
            type = file.type,
            videoNode = document.querySelector('#player'),
            canPlay = videoNode.canPlayType(type);
        // alert(this.files[0]);
        // alert(type);
        // alert(videoNode);
        // alert(canPlay);
        if (canPlay === '') canPlay = 'no';
        var isError = canPlay === 'no';

        if (isError) {
            alert("视频不能播放");
            return;
        }

        var fileURL = URL.createObjectURL(file);
        // alert("fileURL"+fileURL);
        videoNode.src = fileURL;
        videoNode.play();
    }
}
//与客户端交互后的回调函数
var nativeBridgeCallback = {
    cameraCallback: function(srcString){     //表示开通摄像头权限 videoSrc/base64
        alert("摄像头权限已开通并且有返回值");
        // alert(srcString);
        console.log('cameraCallback');
    },
    cameraErrorCallback: function(){    //表示未开通摄像头权限
        alert("摄像头权限未开通或调用失败");
        console.log('cameraErrorCallback');
    },
    photosCallback: function(base64){   //表示开通相册权限
        alert("相册权限已开通并且有返回值");
        // alert(base64);
        // document.querySelector("#img").src = base64;
        console.log('photosCallback');
    },
    photosErrorCallback: function(){    //未开通相册权限
        alert("相册权限未开通或调用失败");
        console.log('photosErrorCallback');
    }
};

var Base = {
    /**
     * [appMovile 判断苹果手机还是安卓手机，添加不同属性使得在小窗口播放]
     * @return {[type]} [description]
     */
    appMovile: function(){
        if(this.ismobile() == 0){  // IOS添加属性使得小窗口播放
            //playsinline="true" webkit-playsinline="true"
            $('#player').attr('playsinline',true);
            $('#player').attr('webkit-playsinline',true);
        }
    },
    /**
     * [ismobile 判断机型 返回0--Ios 返回1--Android]
     * @return {[type]} [description]
     */
    ismobile: function(){
        var u = navigator.userAgent, app = navigator.appVersion;
        if(/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))){
         if(window.location.href.indexOf("?mobile")<0){
          try{
           if(/iPhone|mac|iPod|iPad/i.test(navigator.userAgent)){
            return '0';
           }else{
            return '1';
           }
          }catch(e){}
         }
        }else if( u.indexOf('iPad') > -1){
            return '0';
        }else{
            return '1';
        }
    }
};
//权限相关-------------------------------------------
//调用摄像头相关------------------------------------------- 参数： 1 拍照 2 视频
//调用摄像头
$(document).delegate('.J-camera-btn1','click', function(){
    h5Standard.camera(1);
});
$(document).delegate('.J-camera-btn2','click', function(){
    h5Standard.camera(2);
});
//调用相册相关------------------------------------------- 参数：0 相册全部 1 相册图片 2 相册视频
//调用相册全部
$(document).delegate('.J-photos-btn','click', function(){
    h5Standard.photos(0);
});
//调用相册图片
$(document).delegate('.J-photos-btn1','click', function(){
    h5Standard.photos(1);
});
//调用相册视频
$(document).delegate('.J-photos-btn2','click', function(){
    h5Standard.photos(2);
});
//h5上传图片预览
$('#upload_btn3').live('change',function(){
    var _id=$(this).attr('id'),
        _url=$(this).val(),
        _dom = document.getElementById(_id);
    h5Standard.previewImage(_dom, _id);
});

//h5上传视频预览
Base.appMovile();
$('#upload_btn4').live('change',h5Standard.playSelectedFile);
