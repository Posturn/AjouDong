# Generated by Django 3.0.4 on 2020-05-31 18:36

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('Server_app', '0010_auto_20200601_0321'),
    ]

    operations = [
        migrations.AlterField(
            model_name='testtag',
            name='clubID',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='Server_app.TestTable'),
        ),
    ]