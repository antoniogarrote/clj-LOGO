CANVAS = function(){
    return { w: $("canvas")[0].width, h: $("canvas")[0].height };
};

LOGO = {
    turtle: {"x": 300, "y":200, "angle": 0, "color":0, "pen": true},

    init: function(){
        LOGO.pen = $("canvas")[0].getContext("2d");

        LOGO.pen.fillStyle = "#000"
        LOGO.pen.fillRect(0,0,CANVAS().w, CANVAS().h);

        LOGO.pen.beginPath();
        LOGO.pen.strokeStyle  = LOGO.colorToHex();
        console.debug("x -> "+LOGO.turtle.x + " y -> "+LOGO.turtle.y );
        LOGO.pen.moveTo(LOGO.turtle.x, LOGO.turtle.y);
    },

    sentences: [],

    variables: {},

    colorToHex: function() {
        if(LOGO.turtle.color == 0) {
            return "white";
        } else { if(LOGO.turtle.color == 1) {
            return "#00f";
        } else {if(LOGO.turtle.color == 2) {
            return "#0f0";
        } else {if(LOGO.turtle.color == 3) {
            return "cyan";
        } else {if(LOGO.turtle.color == 4) {
            return "red";
        } else {if(LOGO.turtle.color == 5) {
            return "magenta";
        } else {if(LOGO.turtle.color == 6) {
            return "yellow";
        } else {if(LOGO.turtle.color == 7) {
            return "black";
        } else {if(LOGO.turtle.color == 8) {
            return "brown";
        } else {if(LOGO.turtle.color == 9) {
            return "tan";
        } else {if(LOGO.turtle.color == 10) {
            return "darkGreen";
        } else {if(LOGO.turtle.color == 11) {
            return "aqua";
        } else {if(LOGO.turtle.color == 12) {
            return "salmon";
        } else {if(LOGO.turtle.color == 13) {
            return "purple";
        } else {if(LOGO.turtle.color == 14) {
            return "orange";
        } else {if(LOGO.turtle.color == 15) {
            return "grey";
        } else {
            return "white";
        }}}}}}}}}}}}}}}}
    },

    printTurtle: function() {
        var penp = LOGO.turtle.pen;
        LOGO.turtle.pen = true
        LOGO.left(90);
        LOGO.forward(5);
        LOGO.right(120);
        LOGO.forward(10);
        LOGO.right(120);
        LOGO.forward(10);
        LOGO.right(120);
        LOGO.forward(5);
        LOGO.right(90);
        LOGO.turtle.pen = penp;
    },

    interpreter: function(sentences) {
        for(var i in sentences) {
            LOGO.sentences.push(sentences[i]);
        }

        LOGO.clearScreen();

        LOGO.variables = {};
        LOGO.turtle.x = 300;
        LOGO.turtle.y = 200;
        LOGO.turtle.angle = 0;
        LOGO.turtle.color = 0;
        LOGO.turtle.pen = true;

        LOGO.pen.beginPath();
        LOGO.pen.strokeStyle  = LOGO.colorToHex();
        LOGO.pen.moveTo(LOGO.turtle.x, LOGO.turtle.y);

        LOGO.dispatchSentences(LOGO.sentences);

        LOGO.printTurtle();
    },

    dispatchSentences: function(sentences) {
        for(var i in sentences) {
            var sentence = sentences[i];
            if(sentence.token == "sentence") {
                if(sentence.content["function-call"] != null) {
                    LOGO.interpreteFunctionCall(sentence.content);
                }
            } else { if(sentence.token == "assignation") {
                LOGO.interpreteAssignation(sentence.content);
            }}
        }

    },

    interpreteAssignation: function(assignation) {
        var varName = assignation.lvalue.content;

        if(assignation.rvalue.token == "number") {
            LOGO.variables[varName] = assignation.rvalue.content;
        } else { if(assignation.rvalue.token == "word") {
            var rvalueVarName = assignation.rvalue.content;
            var rvalueVarValue = LOGO.variables[rvalueVarName];
            if(rvalueVarValue == null) {
                alert("there was a problem interpreting your LOGO program, variable " + rvalueVarName + " is not bounded");
                throw("there was a problem interpreting your LOGO program, variable " + rvalueVarName + " is not bounded");
            } else {
                LOGO.variables[varName] = rvalueVarValue;
            }
        } else { if(assignation.rvalue.token == "arithmetic-operation") {
            LOGO.variables[varName] = LOGO.interpreteArithmenticOperation(assignation.rvalue.content);
        }}}
    },

    interpreteArithmenticOperation: function(operation) {
        var opArguments = [];

        for(var i in operation.arguments) {
            var arg = operation.arguments[i];
            if(arg.token == "number") {
                opArguments.push(arg.content);
            } else { if(arg.token == "word") {
                var val = LOGO.variables[arg.content];
                if(val == null) {
                    alert("error interpreting your logo program, variable " + arg.content + " unbounded");
                    throw("error interpreting your logo program, variable " + arg.content + " unbounded");
                } else {
                    opArguments.push(val);
                }
            }}
        }

        if(operation.operation == "+") {
            return opArguments[0] + opArguments[1];
        } else { if(operation.operation == "-") {
            return opArguments[0] - opArguments[1];
        } else { if(operation.operation == "*") {
            return opArguments[0] * opArguments[1];
        } else { if(operation.operation == "/") {
            return opArguments[0] / opArguments[1];
        } else { if(operation.operation == "%") {
            return opArguments[0] % opArguments[1];
        }}}}}
    },

    resolveVar: function(token) {
        if(token.token == "word") {
            if(LOGO.variables[token.content] != null) {
                return LOGO.variables[token.content];
            } else {
                return token.content;
            }
        } else {
            return token.content;
        }
    },

    interpreteFunctionCall: function(functionCall) {

        var functionName = functionCall["function-call"]["fn-name"];
        var args = functionCall.args;

        if(functionName == "forward") {
            LOGO.forward(LOGO.resolveVar(args[0]));
        } else { if(functionName == "right") {
            LOGO.right(LOGO.resolveVar(args[0]));
        } else { if(functionName == "left") {
            LOGO.left(LOGO.resolveVar(args[0]));
        } else { if(functionName == "back") {
            LOGO.back(LOGO.resolveVar(args[0]));
        } else { if(functionName == "clearscreen") {
            LOGO.clearScreen();
        } else { if(functionName == "setpencolor") {
            LOGO.setPenColor(LOGO.resolveVar(args[0]));
        } else { if(functionName == "pendown") {
            LOGO.turtle.pen = true;
        } else { if(functionName == "penup") {
            LOGO.turtle.pen = false;
        } else { if(functionName == "arc") {
            LOGO.arc(LOGO.resolveVar(args[0]), LOGO.resolveVar(args[1]));
        } else { if(functionName == "repeat") {
            if(args[0].token == "number" && args[1].token == "list") {
                LOGO.repeat(args[0].content, args[1].content);
            } else {
                alert("error interpreting repeat, arguments must be a number and a list of sentences");
                throw("error interpreting repeat, arguments must be a number and a list of sentences");
            }
        }}}}}}}}}}
    },

    repeat: function(times, sentences) {
        var content = [];

        for(var i=0; i < sentences.length; i++) {
            var sentence = sentences[i];
            content.push(sentence.content);
        }

        for(var i=0; i < times; i++) {
            LOGO.dispatchSentences(content);
        }
    },

    arc: function(radius, angle) {
        LOGO.pen.beginPath();
        LOGO.pen.strokeStyle  = LOGO.colorToHex();
        angle = angle % 360;

        LOGO.pen.moveTo(LOGO.turtle.x, LOGO.turtle.y);
        if(LOGO.turtle.pen) {
            console.debug("ARC radius: " + radius + " arc init: " + LOGO.turtle.angle + " arc end: " + ((360 - angle) * (Math.PI/180)));
//            LOGO.pen.arc(LOGO.turtle.x, LOGO.turtle.y, radius, (LOGO.turtle.angle * (Math.PI/180)), ((360 + angle) * (Math.PI/180)), false);
            LOGO.pen.arc(LOGO.turtle.x, LOGO.turtle.y, radius, (LOGO.turtle.angle * (Math.PI/180)), ((LOGO.turtle.angle + angle) * (Math.PI/180)), false);
            LOGO.pen.stroke();
        }
    },

    forward: function(pixels){
        LOGO.pen.beginPath();
        LOGO.pen.strokeStyle  = LOGO.colorToHex();
        LOGO.pen.moveTo(LOGO.turtle.x, LOGO.turtle.y);

        console.debug("EXEC: forward "+pixels);
        var x0 = LOGO.turtle.x;
        var y0 = LOGO.turtle.y;
        var angle = LOGO.turtle.angle;

        var xy1 = MATH.endPoint(x0,y0,angle,pixels);
        var x1 = xy1[0];
        var y1 = xy1[1];

        LOGO.turtle.x = x1;
        LOGO.turtle.y = y1;

        if(LOGO.turtle.pen) {
            LOGO.pen.lineTo(x1,y1);
            LOGO.pen.stroke();
        }

        LOGO.pen.moveTo(x1,y1);

        console.debug("x -> "+LOGO.turtle.x + " y -> "+LOGO.turtle.y + " angle -> " + LOGO.turtle.angle);
    },

    back: function(pixels) {
        LOGO.right(180);
        LOGO.forward(pixels);
        LOGO.left(180);
    },

    right: function(angle) {
        console.debug("EXEC: right "+angle);

        if(angle > 360) {
            angle = angle % 360;
        }

        LOGO.turtle.angle += angle;
        if(LOGO.turtle.angle > 360) {
            LOGO.turtle.angle = LOGO.turtle.angle - 360;
        }
        console.debug("x -> "+LOGO.turtle.x + " y -> "+LOGO.turtle.y + " angle -> " + LOGO.turtle.angle);
    },

    left: function(angle) {
        console.debug("EXEC: left "+angle);

        if(angle > 360) {
            angle = angle % 360;
        }

        LOGO.turtle.angle -= angle;
        if(LOGO.turtle.angle < 0) {
            LOGO.turtle.angle =  360 + LOGO.turtle.angle;
        }

        console.debug("x -> "+LOGO.turtle.x + " y -> "+LOGO.turtle.y + " angle -> " + LOGO.turtle.angle);
    },

    clearScreen: function() {
        $("canvas")[0].width = CANVAS().w;
        $("canvas")[0].height = CANVAS().h;
        LOGO.pen.fillStyle = "#000"
        LOGO.pen.fillRect(0,0,CANVAS().w, CANVAS().h);

        LOGO.pen.beginPath();
        LOGO.pen.strokeStyle  = LOGO.colorToHex();
        console.debug("x -> "+LOGO.turtle.x + " y -> "+LOGO.turtle.y );
    },

    setPenColor: function(colorCode) {
        LOGO.turtle.color = colorCode;
        LOGO.pen.beginPath();
        LOGO.pen.strokeStyle = LOGO.colorToHex();
        LOGO.pen.stroke();
    },

    penDown: function() {
        LOGO.turtle.pen = true;
    },

    penUp: function() {
        LOGO.turtle.pen = false;
    }

};

MATH = {
    endPoint: function(x,y,angle,length) {
        var op = Math.sin(angle * (Math.PI/180)) * length;
        var adj = Math.cos(angle * (Math.PI/180)) * length;

        if(angle >=0 && angle < 90) {
            var nx = x + op;
            var ny = y - adj;
        } else {
            if(angle >= 90 && angle < 180) {
                var nx = x + op;
                var ny = y - adj;
            } else {
                if(angle >= 180 && angle < 270) {
                    var nx = x + op;
                    var ny = y - adj;
                } else {
                    if(angle >= 270 && angle <= 360) {
                        var nx = x + op;
                        var ny = y - adj;
                    }
                }
            }
        }

        console.debug("x: " + x + " y: " + y + " angle: " + angle + " length:" + length );
        console.debug("op: " + op + " adj: " + adj);
        console.debug("nx: " + nx + " ny: " + ny);
        return [nx,ny];
    }
}

$(document).ready(function(){
    LOGO.init();
    LOGO.printTurtle();

    if($("#logo-sentence-form")[0] == null) {
        // showing a sketch
        var logoCode = $("#show-sketch-code").text();
        var logoLanguage = $("#sketch-lang").text();

        $.ajax({ type:'POST',
                 url: '/evaluate',
                 data: {"code": logoCode, "logoLanguage":logoLanguage },
                 success: function(msg){
                     $("#spinner").hide();
                     $("#canvas").show();
                     if(msg.result == "success") {
                         LOGO.interpreter(msg.sentences);
                     } else {
                         alert("there was a problem evaluating your LOGO code");
                     }
                 },
                 dataType: "json"});
    } else {
        // editing
        $("#logo-sentence-form").submit(function(){
            var logoCode = $("#logo-sentence").val();
            var logoLanguage =  $("#logo-language").val();

            $.ajax({ type:'POST',
                     url: '/evaluate',
                     data: {"code": logoCode, "logoLanguage":logoLanguage },
                     success: function(msg){
                         if(msg.result == "success") {
                             $("#logo-sentence").val("");
                             var oldSession = $("#sketch-code").val();
                             if(oldSession != "") {
                                 $("#sketch-code").val(oldSession + "\n\n" + logoCode);
                             } else {
                                 $("#sketch-code").val(logoCode);
                             }
                             LOGO.interpreter(msg.sentences);
                         } else {
                             alert("there was a problem evaluating your LOGO code");
                         }
                     },
                     dataType: "json"});

            return false;
        });

        // testing the session
        $("#test-session").click(function(){
            var logoCode = $("#sketch-code").val();
            var logoLanguage =  $("#logo-language").val();

            LOGO.sentences = [];
            LOGO.variables = {};

            $.ajax({ type:'POST',
                     url: '/evaluate',
                     data: {"code": logoCode, "logoLanguage":logoLanguage },
                     success: function(msg){
                         if(msg.result == "success") {
                             LOGO.interpreter(msg.sentences);
                         } else {
                             alert("there was a problem evaluating your LOGO code");
                         }
                     },
                     dataType: "json"});

            return false;
        });

    }
})
