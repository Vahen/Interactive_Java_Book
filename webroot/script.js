		var currentID=-1;
		var loading = "Loading...";
  
		function sendAnswer(id){
			var element = $("#answerFromServer");
			var answerToSend = $("#answer");
			element.html(loading);
			$.post('/validateExercice/'+id+'/'+answerToSend.val(),
					function(data){
						console.log(data);	
						element.html("");
						element.append(data);
					}
				);
		}

		function printExercice(id){
			var element = $("#exercice");
			var respon = $("#respons");
			var sendB = $("#sendButton");
			var answerFromServer = $("#answerFromServer");

			element.html(loading);
			$.get('/exercice/'+id,
				function(data){
					var noData = '<p>There is no Exercice '+id+'.MARKDOWN</p>' ;
					element.html("");
					answerFromServer.html("");
					console.log(data);
					element.append(data);
					if(data.localeCompare(noData) == 0){
						respon.html("");
						sendB.html("");
						answerFromServer.html("");
					}
				}
			);
			currentID = id;
			//alert(currentID);
		}
		
		function printAnswerArea(){
			var respon = $("#respons");
			respon.html("");
			respon.append('<textarea id=\"answer\" rows=20 cols=100 > Write your answer in here !</textarea>');
		}
		
		function printAnswerButton(id){
			var sendB = $("#sendButton");			
			sendB.html("");
			sendB.append('<input id=Send_answer type=button value=Send_answer onclick=sendAnswer('+id+');'+' </input>');
		}
		
		function printButtons(){
			var buttons = $("#buttons");
			buttons.html("");
			$.get('/countfiles',
				function(data){
					for(i=0 ;i< parseInt(data);i++){
						buttons.append('<input id=button'+i+' type=button '+' value=Exercice_'+i+' onclick=printExercice('+i+');printAnswerArea();printAnswerButton('+i+');'+' </input>');
					}
				}
			);		
		}
		
		function testModify(){
			var element = $("#exercice");
			var id = currentID;
			if(id == -1){
				return;
			}
			
			//alert("testModify");
			$.get('/watcherModify/'+id,
				function(data){
					var noData = '' ;
					if(data.localeCompare(noData) != 0){
						element.html("");
						console.log(data);
						element.append(data);
					}
				}
			);
		}
		
		$(document).ready(function(){
			printButtons();
			testModify();
			var interval = 1000 * 60 * 1;
			setInterval(printButtons,interval);
			setInterval(testModify,10000);
		});