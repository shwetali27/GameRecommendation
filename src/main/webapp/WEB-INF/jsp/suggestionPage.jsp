<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Suggestions Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
	<div class="container">
		<div class="row">
			<c:forEach items="${suggestionList}" var="suggestion">
				<div class="col-lg-3 col-md-4 col-sm-6 col-xs-6"
					style="margin-top: 10px;">
					<img class="card-img-top" src="${suggestion.mImgUrl}"
						alt="Card image cap" height="180px">
					<div class="card-block">
						<h4 class="card-title">${suggestion.mContentName}</h4>
					</div>
					<p class="card-text">${suggestion.mCategoryName}</p>
				</div>
			</c:forEach>
		</div>
	</div>
</body>
</html>