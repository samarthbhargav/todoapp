<#include "header.ftl">

    <div class="container">
    <div class="row vertical-center-row">
        <div class="col-lg-12">
            <div class="row">
                <div class="col-xs-4 col-xs-offset-4">
                	<#if message??>
                		<p class="text-success">${message}</p>	
                	</#if>                	
                	<h1> TODO App </h1>
                	<h2> Get Stuff Done! </h2>
                	<hr>
                	<#if username??>
                		<a href="/home"><button type="button" class="btn btn-primary">Home</button></a>
                	<#else>
	                	<a href="/login"><button type="button" class="btn btn-primary">Log In</button></a>
	                	<h4>Or</h4>
	                	<a href="/signup"><button type="button" class="btn btn-primary">Sign Up</button></a>
	                	<hr>
                	</#if>                	
				</div>
            </div>
        </div>
    </div>
</div>


<#include "footer.ftl">