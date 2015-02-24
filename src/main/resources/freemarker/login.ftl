<#include "header.ftl">

<div class="container">
	<div class="row">
	  <div class="col-md-4"></div>
	  <div class="col-md-4">
	  	<#if message??>
	  		<p class="text-danger">${message}</p>
	  	</#if>	  	
	  	
	  	<form id="inp-form" action="/login" method="post">
		  <div class="form-group">
		    <label for="username">User Name (email)</label>
		    <input type="email" class="form-control" id="username" name="username" placeholder="Enter Username (email)">
		  </div>
		  <div class="form-group">
		    <label for="password">Password</label>
		    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
		  </div>
		  <button type="submit" class="btn btn-default">Submit</button>
		</form>
	  </div>
	  <div class="col-md-4"></div>
	</div>
</div>


<#include "footer.ftl">