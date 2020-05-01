from rest_framework import serializers, viewsets
from Server_app.models import userAccount

class TestSerializer(serializers.ModelSerializer):
    class Meta:
        model=userAccount
        fields=('uID', 'uPW', )

class TestViewSet(viewsets.ModelViewSet):
    queryset = userAccount.objects.all()
    serializer_class = TestSerializer