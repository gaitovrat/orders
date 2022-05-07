<%@ page import="vsb.gai0010.servlet.Attribute" %>
<nav class="bg-gray-800">
    <div class="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
        <div class="relative flex items-center justify-between h-16">
            <div class="flex-1 flex items-center justify-center sm:items-stretch sm:justify-start">
                <a href="${pageContext.request.contextPath}/"
                   class="text-gray-300 px-3 py-2 rounded-md text-lg font-bold">Orders</a>
            </div>
            <div class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                <div class="ml-3 relative">
                    <%
                        if (session.getAttribute(Attribute.USER.toString()) == null) {
                    %>
                    <a href="${pageContext.request.contextPath}/signin.jsp"
                       class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-2 rounded-md text-sm font-medium">Sign
                        in</a>
                    <a href="#"
                       class="ml-3 bg-blue-500 hover:bg-blue-600 text-white px-3 py-2 rounded-md text-sm font-medium">Register</a>
                    <%
                    } else {
                    %>
                    <a href="${pageContext.request.contextPath}/orders.jsp"
                       class="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Shopping
                        cart</a>
                    <a href="${pageContext.request.contextPath}/profile.jsp"
                       class="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Profile</a>
                    <a href="${pageContext.request.contextPath}/logout"
                       class="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Log out</a>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>
</nav>
