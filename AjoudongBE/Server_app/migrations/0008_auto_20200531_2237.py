# Generated by Django 3.0.4 on 2020-05-31 13:37

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('Server_app', '0007_auto_20200530_1401'),
    ]

    operations = [
        migrations.AlterField(
            model_name='appliedclublist',
            name='memberState',
            field=models.IntegerField(),
        ),
    ]
