package com.github.ffch.jpamapper.core.sql.type.insert;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.sql.helper.DefaultSqlHelper;
import com.github.ffch.jpamapper.core.sql.type.AbstractPrecisSqlType;

public class SaveAllSqlType extends AbstractPrecisSqlType {

	public static final SaveAllSqlType INSTANCE = new SaveAllSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.insertSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.valuesCollectionSql(jpaModelEntity, false));
		sql.append(" </script>");
		return sql.toString().trim();
	}

}