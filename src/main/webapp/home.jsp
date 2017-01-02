<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Home Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
	<form class="form-horizontal" action="getContentName" method="post">

		<div class="control-group">
			<label class="control-label">Game Name:</label>
			<div class="controls">
				<input type="text" name=pContentName id="pContentName"
					title="Content Name" value="">
			</div>
		</div>
		<button type="submit" class="btn btn-primary">Submit</button>
		<!-- <div>
			<div class="form-group">
				<label for="gameName">Game Name:</label> <input type="text"
					class="form-control" id="gameName">
			</div>
		</div>
		<button type="submit" class="btn btn-primary">Submit</button> -->
	</form>
</body>
</html>