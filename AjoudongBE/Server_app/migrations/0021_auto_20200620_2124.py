# Generated by Django 3.0.4 on 2020-06-20 12:24

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('Server_app', '0020_auto_20200616_1604'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='ads',
            name='adsrestViews',
        ),
        migrations.AlterField(
            model_name='ads',
            name='adsID',
            field=models.AutoField(primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='club',
            name='clubID',
            field=models.AutoField(primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='club',
            name='clubIMG',
            field=models.CharField(max_length=128, null=True),
        )
    ]