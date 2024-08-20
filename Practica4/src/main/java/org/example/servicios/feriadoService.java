package org.example.servicios;

import org.example.DTO.Contador;
import org.example.DTO.DiaDTO;
import org.example.DTO.DiaFeriado;
import org.example.connections.HibernateUtil;
import org.example.entidades.dia;
import org.example.entidades.feriado;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class feriadoService {
    public boolean altaFeriado(DiaFeriado diaFeriado){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            String hql = "FROM dia f WHERE f.dia = :dia AND f.mes = :mes AND f.anio = :anio";
            Query<dia> query = session.createQuery(hql, dia.class);
            query.setParameter("dia", diaFeriado.getDia());
            query.setParameter("mes", diaFeriado.getMes());
            query.setParameter("anio", diaFeriado.getAnio());
            dia dia = query.uniqueResult();
            if(dia!=null) {
                String hqlFeriado = "FROM feriado f WHERE f.dia_trabajo_id = :dia_trabajo_id";
                Query<feriado> feriadoQuery = session.createQuery(hqlFeriado,feriado.class);
                query.setParameter("dia_trabajo_id", dia.getId());
                feriado existFeriado = feriadoQuery.uniqueResult();
                if(existFeriado==null){
                    feriado feriado = new feriado("nuevoferiado",dia);
                    session.save(feriado);
                    session.getTransaction().commit();
                    return true;
                }
                return false;
            }
            return false;
        }catch (Exception e){
            throw new RuntimeException("no se pudo dar de alta el feriao ", e);
        }
    }

    public boolean esFeriado(DiaFeriado diaFeriado){
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();

            String hql = "FROM feriado f WHERE f.dia_trabajo_id.dia = :dia AND f.dia_trabajo_id.mes = :mes AND f.dia_trabajo_id.anio = :anio";
            Query<feriado> query = session.createQuery(hql, feriado.class);
            query.setParameter("dia", diaFeriado.getDia());
            query.setParameter("mes", diaFeriado.getMes());
            query.setParameter("anio", diaFeriado.getAnio());

            feriado feriado = query.uniqueResult();
            session.getTransaction().commit();

            if(feriado != null){
                return true;
            }

            return false;
        }catch (Exception e){
            throw new RuntimeException("no se pudo ver si es feriado o no", e);
        }
    }

    public List<DiaDTO> diasLaboralesEntreFechas(DiaFeriado diaStart, DiaFeriado diaFin){
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            String hql = "FROM dia d WHERE (d.anio > :anioInicio OR (d.anio = :anioInicio AND d.mes > :mesInicio) OR (d.anio = :anioInicio AND d.mes = :mesInicio AND d.dia >= :diaInicio)) AND (d.anio < :anioFin OR (d.anio = :anioFin AND d.mes < :mesFin) OR (d.anio = :anioFin AND d.mes = :mesFin AND d.dia <= :diaFin)) AND d.fin_semana = false AND d.id NOT IN (SELECT f.dia_trabajo_id.id FROM feriado f)";
            Query<dia> query = session.createQuery(hql,dia.class);
            query.setParameter("anioInicio", diaStart.getAnio());
            query.setParameter("anioFin", diaFin.getAnio());
            query.setParameter("mesFin", diaFin.getMes());
            query.setParameter("mesInicio",diaStart.getMes());
            query.setParameter("diaInicio", diaStart.getDia());
            query.setParameter("diaFin", diaFin.getDia());

            List<dia> diasLaborales = query.getResultList();
            session.getTransaction().commit();

            List<DiaDTO> diasDto = new ArrayList<>();
            for (dia dia : diasLaborales){
                DiaDTO diaDTO = new DiaDTO();
                diaDTO.setId(dia.getId());
                diaDTO.setFecha(dia.getDia() + " " + dia.getMes() + " " + dia.getAnio());
                diasDto.add(diaDTO);
            }
            return diasDto;
        }catch (Exception e){
            throw new RuntimeException("no se pudo obtener la lista entre esas fechas ", e);
        }
    }

    public List<Contador> cantidadaFeriadosPorMes(int anio){
        try(Session session = HibernateUtil.getSession()){
            session.beginTransaction();

            String hql = "SELECT f.dia_trabajo_id.mes, COUNT(f.id) FROM feriado f " +
                    "WHERE f.dia_trabajo_id.anio = :anio " +
                    "GROUP BY f.dia_trabajo_id.mes " +
                    "ORDER BY COUNT(f.id) DESC";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("anio", anio);

            List<Object[]> results = query.getResultList();
            session.getTransaction().commit();

            List<Contador> contadores = new ArrayList<>();
            for(Object[] o : results){
                Contador contador = new Contador();
                contador.setMes((int) o[0]);
                contador.setCantidad(((Long) o[1]).intValue());
                contadores.add(contador);
            }
            return contadores;
        }catch (Exception e){
            throw new RuntimeException("no se pudo obtener la lista de feriados por mes de ese anio ", e);
        }
    }

    public double promedioDiasLaborales(int mes){
        try(Session session= HibernateUtil.getSession()){
            session.beginTransaction();

            String hqlTotalDias = "SELECT COUNT(d.id) FROM dia d WHERE d.mes = :mes";
            Query<Long> queryTotalDias = session.createQuery(hqlTotalDias, Long.class);
            queryTotalDias.setParameter("mes", mes);

            String hqlDiasLaborales = "SELECT COUNT(d.id) FROM dia d WHERE d.mes = :mes AND d.fin_semana = false AND d.id NOT IN (SELECT f.dia_trabajo_id.id FROM feriado f WHERE f.dia_trabajo_id.mes = :mes)";
            Query<Long> queryDiasLaborales = session.createQuery(hqlDiasLaborales, Long.class);
            queryDiasLaborales.setParameter("mes", mes);

            Long totalDias = queryTotalDias.uniqueResult();
            Long diasLaborales = queryDiasLaborales.uniqueResult();
            session.getTransaction().commit();

            double promedio = 0.0;

            if (totalDias > 0) {
                promedio = ((double) diasLaborales / totalDias)*100;
                return promedio;
            }

            return promedio;
        }catch (Exception e){
            throw new RuntimeException("no se pudo obtener el promedio ", e);
        }
    }

}
