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
		<a href="/completed">
	  	<button type="button" class="btn btn-success btn-lg">
  			<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> View Completed
		</button>
		</a>
	  	<h4> ${username}'s Todo list </h4>
		<table class="table table-hover">
			<tr>				
				<td class="bg-primary">Item</td>		
				<td class="bg-primary">Tags</td>
				<td class="bg-primary">Created On</td>
				<td class="bg-primary">Complete</td>
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
					<td><button type="button" class="btn btn-success btn-xs" onclick="postToURL('/complete/${todo.id}')"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span></button></td>
				</tr>
	  		</#list>			
		</table>
	  </div>
	  <div class="col-md-2"></div>
	</div>
</div>


<#include "footer.ftl">