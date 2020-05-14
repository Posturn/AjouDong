from django.contrib import admin
from Server_app.models import userAccount, managerAccount
# Register your models here.


admin.site.register(userAccount)
admin.site.register(managerAccount)