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
		<a href="/home"><button type="button" class="btn btn-success btn-lg">Home</button></a>
		<h4> ${username}'s Completed Todos</h4>	  	
		<table class="table table-hover">
			<tr>				
				<td class="bg-primary">Item</td>		
				<td class="bg-primary">Tags</td>
				<td class="bg-primary">Created On</td>
				<td class="bg-primary">Completed On</td>
				<td class="bg-primary">Add to Todo</td>
			</tr>
			<#list todolist as todo>	  			
	  			<tr>				
					<td>${todo.description}</td>
					<td>
						<#list todo.tags as tag>
							<a href="/tag/${tag}" ><span class="label label-default">${tag}</span></a>
						</#list>
					</td>
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