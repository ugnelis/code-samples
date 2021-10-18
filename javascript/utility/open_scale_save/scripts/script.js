(function (window) {
    var canvas = this.__canvas = new fabric.Canvas('canvas', {selection: false});

    // Buttons
    var openFileButton = document.getElementById('open-file');
    var saveFileButton = document.getElementById('save-file');

    // Inputs
    var scaleInput = document.getElementById("scale");
    var fileNameInput = document.getElementById("fileName");


    // Fieldsets
    var fileFieldset = document.getElementById("fileFieldset");

    var minScale = 320;
    var maxScale = 600;

    var width;
    var height;

    function handleFileSelect(object) {
        var file = object.files[0];

        // Only process image file.
        if (!file.type.match('image.*')) {
            return;
        }

        var reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (file) {
            return function (event) {
                var image = new Image();
                image.src = event.target.result;

                // Set canvas size as the image size.
                image.onload = function () {
                    canvas.setDimensions({width: this.width, height: this.height});
                    scaleInput.max = image.width;
                    fileNameInput.value = file.name.substr(0, file.name.lastIndexOf('.')); // remove file extension
                };

                canvas.setBackgroundImage(image.src, canvas.renderAll.bind(canvas), {
                    originX: 'left',
                    originY: 'top',
                    left: 0,
                    top: 0
                });
            };

        })(file);
        // Read in the image file as a data URL.
        reader.readAsDataURL(file);
    }

    function handleFileSave() {

        var scaledImage = canvas.backgroundImage.toDataURL({format: 'jpeg'});
        var base64image = scaledImage.replace(/^data:image\/(png|jpeg);base64,/, "");

        var binary = convertBinaryToUnicode(atob(base64image));
        var imageBlob = new Blob([binary], {type: 'image/jpeg'});
        // Open Dialog for image
        saveAs(imageBlob, fileNameInput.value + ".jpg");

        canvas.deactivateAll().renderAll();
    }

    function convertBinaryToUnicode(binaryImage) {
        var length = binaryImage.length;
        var buffer = new ArrayBuffer(length);
        var array = new Uint8Array(buffer);
        for (var i = 0; i < length; i++) {
            array[i] = binaryImage.charCodeAt(i);
        }
        return buffer;
    }

    function scale() {
        width = scaleInput.value;
        height = width * canvas.height / canvas.width;
        resizeCanvas(width, height);
    }

    function resizeCanvas(width, height) {
        canvas.backgroundImage.scaleToWidth(width);
        canvas.backgroundImage.scaleToHeight(height);
        canvas.setDimensions({width: width, height: height});
        canvas.renderAll();
    }

    scaleInput.min = minScale;
    scaleInput.max = maxScale;

    window.handleFileSelect = handleFileSelect;
    window.handleFileSave = handleFileSave;
    window.scale = scale;

})(window);
