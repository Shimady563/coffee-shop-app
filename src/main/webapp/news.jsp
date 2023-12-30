<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>News</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/news.css">
</head>
<body>
<nav class="navbar">
    <h1><a class="navbar-brand" href="home">Coffee Shop</a></h1>
    <form class="form-inline my-2 my-lg-0 ml-auto" action="news" method="post">
        <input class="form-control mr-2" type="search" placeholder="Enter news title" aria-label="Search">
        <button class="btn btn-outline-success my-2" type="submit">Search</button>
    </form>
</nav>
<div class="container mt-4">
    <div class="row">
        <div class="col-md-4">
            <a href="piece-of-news.jsp" class="news-block">
                <img src="img/coffee.webp" alt="News 1" class="img-fluid">
                <p>Long title for line wrap test and many other meaningless words</p>
            </a>
        </div>
        <div class="col-md-4">
            <a href="#" class="news-block">
                <img src="img/coffee.webp" alt="News 2" class="img-fluid">
                <p>News 2</p>
            </a>
        </div>
        <div class="col-md-4">
            <a href="#" class="news-block">
                <img src="img/coffee.webp" alt="News 3" class="img-fluid">
                <p>News 3</p>
            </a>
        </div>
        <div class="col-md-4">
            <a href="#" class="news-block">
                <img src="img/coffee.webp" alt="News 4" class="img-fluid">
                <p>News 4</p>
            </a>
        </div>
    </div>
</div>
</body>
</html>
