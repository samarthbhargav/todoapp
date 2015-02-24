<#include "header.ftl">


<div class="container">
	<div class="row">
	  <div class="col-md-4"></div>
	  <div class="col-md-4">
	  	<#if message??>
	  		<p class="text-danger">${message}</p>
	  	</#if>	  	
	  		  		  	
	  	<form class="form-horizontal" action="/add" method="post">
	  	  <legend>Add a Todo</legend>
	  	  <div class="form-group">
		    <label for="Task">Task</label>
		    <input type="text" class="form-control" id="task" name="task" placeholder="Task">
		  </div>
		  <div class="form-group">
		    <label for="tags">Tags (Comma Seperated)</label>
		    <input type="tags" class="form-control" id="tags" name="tags" placeholder="Tags">
		  </div>
		  <button type="submit" class="btn btn-primary">Submit</button>
		  <button type="reset" class="btn btn-info" onclick="location.href = '/home';">Cancel</button>
		</form>
	  </div>
	  <div class="col-md-4"></div>
	</div>
</div>



<#include "footer.ftl">