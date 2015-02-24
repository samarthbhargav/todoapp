<#include "header.ftl">


<#assign objectConstructor = "freemarker.template.utility.ObjectConstructor"?new()>
<#assign mmddyy = objectConstructor("java.text.SimpleDateFormat","dd/MM/yyyy")>

<div class="container">
	<div class="row">
	  <div class="col-md-2"></div>
	  <div class="col-md-8">
	  	<a href="/add">
	  	<button type="button" class="btn btn-default btn-lg">
  			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add
		</button>
		</a>
		<a href="/home">
	  	<button type="button" class="btn btn-success btn-lg">
  			Home
		</button>
		</a>
	  	<h4> Tasks with tag: <span class="label label-default">${tag}</span></h4>
		<table class="table table-hover">
			<tr>				
				<td class="bg-primary">Item</td>						
				<td class="bg-primary">Created On</td>
				<td class="bg-primary">Complete</td>
			</tr>
			<#list incomplete as todo>	  			
	  			<tr>				
					<td>${todo.description}</td>					
					<td>${mmddyy.format(todo.dateCreated)}</td>					
					<td><button type="button" class="btn btn-success btn-xs" onclick="postToURL('/complete/${todo.id}')"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span></button></td>
				</tr>
	  		</#list>			
		</table>
		<hr>
		<h2> Completed </h2>
		<table class="table table-hover">
			<tr>				
				<td class="bg-primary">Item</td>						
				<td class="bg-primary">Created On</td>
				<td class="bg-primary">Completed On</td>
				<td class="bg-primary">Add to Todo</td>
			</tr>
			<#list complete as todo>	  			
	  			<tr>				
					<td>${todo.description}</td>					
					<td>${mmddyy.format(todo.dateCreated)}</td>					
					<td>${mmddyy.format(todo.dateCompleted)}</td>
					<td><button type="button" class="btn btn-success btn-xs" onclick="postToURL('/uncomplete/${todo.id}')"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>Add to Todo</button></td>
				</tr>
	  		</#list>			
		</table>
	  </div>
	  <div class="col-md-2"></div>
	</div>
</div>


<#include "footer.ftl">