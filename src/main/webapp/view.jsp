<%@ page import="vsb.gai0010.model.Cloth" %>
<%@ page import="vsb.gai0010.orm.ClothSQL" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Cloth cloth = new ClothSQL().select(Integer.parseInt(request.getParameter("id")));
    request.setAttribute("cloth", cloth);
%>

<html>
<head>
    <jsp:include page="base/head.jsp" />
    <title>Orders - view</title>
</head>
<body>
    <jsp:include page="base/header.jsp" />
    <div>
        <div class="overflow-y-auto">
            <div class="flex min-h-screen text-center md:block md:px-2 lg:px-4" style="font-size: 0">
                <div class="flex text-base text-left transform transition w-full md:inline-block md:max-w-2xl md:px-4 md:my-8 md:align-middle lg:max-w-4xl">

                    <div class="w-full grid grid-cols-1 gap-y-8 gap-x-6 items-start sm:grid-cols-12 lg:gap-x-8">
                        <div class="aspect-w-2 aspect-h-3 rounded-lg overflow-hidden sm:col-span-4 lg:col-span-5 shadow-2xl">
                            <img src="${pageContext.request.contextPath}/img/base.jpg"
                                 alt="cloth with id = ${cloth.id}" class="object-center object-cover">
                        </div>
                        <div class="sm:col-span-8 lg:col-span-7">
                            <h2 class="text-2xl font-extrabold text-gray-900 sm:pr-12">${cloth.name}</h2>

                            <section aria-labelledby="information-heading" class="mt-2">
                                <h3 id="information-heading" class="sr-only">Product information</h3>

                                <p class="text-2xl text-gray-900">$${cloth.price}</p>
                            </section>

                            <section aria-labelledby="options-heading" class="mt-10">
                                <form>
                                    <h3 class="text-lg font-extrabold text-gray-900 sm:pr-12">Description</h3>
                                    <div>${cloth.description}</div>
                                    <button type="submit"
                                            class="mt-6 w-full bg-blue-600 border border-transparent rounded-md py-3 px-8 flex items-center justify-center text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">Add
                                        to bag</button>
                                </form>
                            </section>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
