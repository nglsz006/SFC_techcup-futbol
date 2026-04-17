package edu.dosw.project.SFC_TechUp_Futbol;

import edu.dosw.project.SFC_TechUp_Futbol.core.model.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.util.Base64Util;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.state.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.mapper.*;
import edu.dosw.project.SFC_TechUp_Futbol.persistence.repository.*;
import edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestMappers {

    public static TorneoMapper torneoMapper() {
        return new TorneoMapper() {
            public TorneoEntity toEntity(Torneo t) {
                if (t == null) return null;
                TorneoEntity e = new TorneoEntity();
                e.setId(t.getId()); e.setNombre(t.getNombre());
                e.setFechaInicio(t.getFechaInicio()); e.setFechaFin(t.getFechaFin());
                e.setCantidadEquipos(t.getCantidadEquipos()); e.setCosto(t.getCosto());
                e.setEstado(t.getEstado()); e.setReglamento(t.getReglamento());
                e.setCierreInscripciones(t.getCierreInscripciones());
                e.setCanchas(t.getCanchas()); e.setHorarios(t.getHorarios());
                e.setSanciones(t.getSanciones());
                return e;
            }
            public Torneo toDomain(TorneoEntity e) {
                if (e == null) return null;
                Torneo t = new Torneo();
                t.setId(e.getId()); t.setNombre(e.getNombre());
                t.setFechaInicio(e.getFechaInicio()); t.setFechaFin(e.getFechaFin());
                t.setCantidadEquipos(e.getCantidadEquipos()); t.setCosto(e.getCosto());
                t.setEstado(e.getEstado()); t.setReglamento(e.getReglamento());
                t.setCierreInscripciones(e.getCierreInscripciones());
                t.setCanchas(e.getCanchas()); t.setHorarios(e.getHorarios());
                t.setSanciones(e.getSanciones());
                t.setEstadoObj(resolverEstado(e.getEstado()));
                return t;
            }
        };
    }

    public static EquipoMapper equipoMapper() {
        return new EquipoMapper() {
            public EquipoEntity toEntity(Equipo eq) {
                if (eq == null) return null;
                EquipoEntity e = new EquipoEntity();
                e.setId(eq.getId()); e.setNombre(eq.getNombre());
                e.setEscudo(eq.getEscudo()); e.setColorPrincipal(eq.getColorPrincipal());
                e.setColorSecundario(eq.getColorSecundario()); e.setCapitanId(eq.getCapitanId());
                e.setJugadores(eq.getJugadores());
                return e;
            }
            public Equipo toDomain(EquipoEntity e) {
                if (e == null) return null;
                Equipo eq = new Equipo();
                eq.setId(e.getId()); eq.setNombre(e.getNombre());
                eq.setEscudo(e.getEscudo()); eq.setColorPrincipal(e.getColorPrincipal());
                eq.setColorSecundario(e.getColorSecundario()); eq.setCapitanId(e.getCapitanId());
                eq.setJugadores(e.getJugadores());
                return eq;
            }
        };
    }

    public static JugadorMapper jugadorMapper() {
        return new JugadorMapper() {
            public JugadorEntity toEntity(Jugador j) {
                if (j == null) return null;
                JugadorEntity e = new JugadorEntity();
                e.setId(j.getId()); e.setName(j.getName());
                e.setEmail(Base64Util.encode(j.getEmail())); e.setPassword(j.getPassword());
                e.setUserType(j.getUserType()); e.setJerseyNumber(j.getJerseyNumber());
                e.setPosition(j.getPosition()); e.setAvailable(j.isAvailable());
                e.setPhoto(j.getPhoto()); e.setEquipoId(j.getEquipo());
                return e;
            }
            public Jugador toDomain(JugadorEntity e) {
                if (e == null) return null;
                Jugador j = new Jugador();
                j.setId(e.getId()); j.setName(e.getName());
                j.setEmail(Base64Util.decode(e.getEmail())); j.setPassword(e.getPassword());
                j.setUserType(e.getUserType()); j.setJerseyNumber(e.getJerseyNumber());
                j.setPosition(e.getPosition()); j.setAvailable(e.isAvailable());
                j.setPhoto(e.getPhoto()); j.setEquipo(e.getEquipoId());
                return j;
            }
        };
    }

    public static AlineacionMapper alineacionMapper() {
        return new AlineacionMapper() {
            public AlineacionEntity toEntity(Alineacion a) {
                if (a == null) return null;
                AlineacionEntity e = new AlineacionEntity();
                e.setId(a.getId()); e.setEquipoId(a.getEquipoId());
                e.setPartidoId(a.getPartidoId()); e.setFormacion(a.getFormacion());
                e.setTitulares(a.getTitulares()); e.setReservas(a.getReservas());
                return e;
            }
            public Alineacion toDomain(AlineacionEntity e) {
                if (e == null) return null;
                Alineacion a = new Alineacion();
                a.setId(e.getId()); a.setEquipoId(e.getEquipoId());
                a.setPartidoId(e.getPartidoId()); a.setFormacion(e.getFormacion());
                a.setTitulares(e.getTitulares()); a.setReservas(e.getReservas());
                return a;
            }
        };
    }

    public static RegistroAuditoriaMapper registroAuditoriaMapper() {
        return new RegistroAuditoriaMapper() {
            public RegistroAuditoriaEntity toEntity(RegistroAuditoria r) {
                if (r == null) return null;
                RegistroAuditoriaEntity e = new RegistroAuditoriaEntity();
                e.setId(r.getId()); e.setAdministradorId(r.getAdministradorId());
                e.setUsuario(r.getUsuario()); e.setTipoAccion(r.getTipoAccion());
                e.setDescripcion(r.getDescripcion()); e.setFecha(r.getFecha());
                return e;
            }
            public RegistroAuditoria toDomain(RegistroAuditoriaEntity e) {
                if (e == null) return null;
                RegistroAuditoria r = new RegistroAuditoria();
                r.setId(e.getId()); r.setAdministradorId(e.getAdministradorId());
                r.setUsuario(e.getUsuario()); r.setTipoAccion(e.getTipoAccion());
                r.setDescripcion(e.getDescripcion()); r.setFecha(e.getFecha());
                return r;
            }
        };
    }

    public static AdministradorMapper administradorMapper() {
        return new AdministradorMapper() {
            public AdministradorEntity toEntity(Administrador a) {
                if (a == null) return null;
                AdministradorEntity e = new AdministradorEntity();
                e.setId(a.getId()); e.setName(a.getName());
                e.setEmail(Base64Util.encode(a.getEmail())); e.setPassword(a.getPassword());
                e.setUserType(a.getUserType()); e.setActivo(a.isActivo());
                return e;
            }
            public Administrador toDomain(AdministradorEntity e) {
                if (e == null) return null;
                Administrador a = new Administrador();
                a.setId(e.getId()); a.setName(e.getName());
                a.setEmail(Base64Util.decode(e.getEmail())); a.setPassword(e.getPassword());
                a.setUserType(e.getUserType()); a.setActivo(e.isActivo());
                return a;
            }
        };
    }

    public static UsuarioRegistradoMapper usuarioRegistradoMapper() {
        return new UsuarioRegistradoMapper() {
            public UsuarioRegistradoEntity toEntity(UsuarioRegistrado u) {
                if (u == null) return null;
                UsuarioRegistradoEntity e = new UsuarioRegistradoEntity();
                e.setId(u.getId()); e.setName(u.getName());
                e.setEmail(Base64Util.encode(u.getEmail())); e.setPassword(u.getPassword());
                e.setUserType(u.getUserType());
                return e;
            }
            public UsuarioRegistrado toDomain(UsuarioRegistradoEntity e) {
                if (e == null) return null;
                UsuarioRegistrado u = new UsuarioRegistrado();
                u.setId(e.getId()); u.setName(e.getName());
                u.setEmail(Base64Util.decode(e.getEmail())); u.setPassword(e.getPassword());
                u.setUserType(e.getUserType());
                return u;
            }
        };
    }

    public static PagoMapper pagoMapper(EquipoMapper equipoMapper) {
        return new PagoMapper() {
            public PagoEntity toEntity(Pago p) {
                if (p == null) return null;
                PagoEntity e = new PagoEntity();
                e.setId(p.getId()); e.setComprobante(Base64Util.encode(p.getComprobante()));
                e.setFechaSubida(p.getFechaSubida()); e.setEstado(p.getEstado());
                e.setEquipo(equipoMapper.toEntity(p.getEquipo()));
                return e;
            }
            public Pago toDomain(PagoEntity e) {
                if (e == null) return null;
                Pago p = new Pago();
                p.setId(e.getId()); p.setComprobante(Base64Util.decode(e.getComprobante()));
                p.setFechaSubida(e.getFechaSubida()); p.setEstado(e.getEstado());
                p.setState(resolverEstado(e.getEstado()));
                p.setEquipo(equipoMapper.toDomain(e.getEquipo()));
                return p;
            }
        };
    }

    public static PartidoMapper partidoMapper(JugadorMapper jugadorMapper) {
        PartidoMapper mapper = new PartidoMapper() {
            public PartidoEntity toEntity(Partido partido) {
                if (partido == null) return null;
                PartidoEntity e = new PartidoEntity();
                e.setId(partido.getId()); e.setFecha(partido.getFecha());
                e.setCancha(partido.getCancha()); e.setMarcadorLocal(partido.getMarcadorLocal());
                e.setMarcadorVisitante(partido.getMarcadorVisitante()); e.setEstado(partido.getEstado());
                e.setFase(partido.getFase());
                if (partido.getTorneo() != null) e.setTorneo(torneoMapper().toEntity(partido.getTorneo()));
                if (partido.getEquipoLocal() != null) e.setEquipoLocal(equipoMapper().toEntity(partido.getEquipoLocal()));
                if (partido.getEquipoVisitante() != null) e.setEquipoVisitante(equipoMapper().toEntity(partido.getEquipoVisitante()));
                mapGolesYEstadoToEntity(partido, e);
                return e;
            }
            public Partido toDomain(PartidoEntity e) {
                if (e == null) return null;
                Partido p = new Partido();
                p.setId(e.getId()); p.setFecha(e.getFecha());
                p.setCancha(e.getCancha()); p.setMarcadorLocal(e.getMarcadorLocal());
                p.setMarcadorVisitante(e.getMarcadorVisitante()); p.setEstado(e.getEstado());
                p.setFase(e.getFase());
                if (e.getTorneo() != null) p.setTorneo(torneoMapper().toDomain(e.getTorneo()));
                if (e.getEquipoLocal() != null) p.setEquipoLocal(equipoMapper().toDomain(e.getEquipoLocal()));
                if (e.getEquipoVisitante() != null) p.setEquipoVisitante(equipoMapper().toDomain(e.getEquipoVisitante()));
                mapGolesYEstadoToDomain(e, p);
                return p;
            }
        };
        ReflectionTestUtils.setField(mapper, "jugadorMapper", jugadorMapper);
        return mapper;
    }

    public static OrganizadorMapper organizadorMapper(TorneoJpaRepository torneoRepo, TorneoMapper torneoMapper) {
        OrganizadorMapper mapper = new OrganizadorMapper() {
            public OrganizadorEntity toEntity(Organizador o) {
                if (o == null) return null;
                OrganizadorEntity e = new OrganizadorEntity();
                e.setId(o.getId()); e.setName(o.getName());
                e.setEmail(Base64Util.encode(o.getEmail())); e.setPassword(o.getPassword());
                e.setUserType(o.getUserType());
                if (o.getCurrentTournament() != null) e.setTorneoId(o.getCurrentTournament().getId());
                return e;
            }
            public Organizador toDomain(OrganizadorEntity e) {
                if (e == null) return null;
                Organizador o = new Organizador();
                o.setId(e.getId()); o.setName(e.getName());
                o.setEmail(Base64Util.decode(e.getEmail())); o.setPassword(e.getPassword());
                o.setUserType(e.getUserType());
                if (e.getTorneoId() != null) {
                    torneoRepo.findById(e.getTorneoId())
                            .ifPresent(t -> o.setCurrentTournament(torneoMapper.toDomain(t)));
                }
                return o;
            }
        };
        ReflectionTestUtils.setField(mapper, "torneoJpaRepository", torneoRepo);
        ReflectionTestUtils.setField(mapper, "torneoMapper", torneoMapper);
        return mapper;
    }

    public static ArbitroMapper arbitroMapper(PartidoMapper partidoMapper) {
        ArbitroMapper mapper = new ArbitroMapper() {
            public ArbitroEntity toEntity(Arbitro a) {
                if (a == null) return null;
                ArbitroEntity e = new ArbitroEntity();
                e.setId(a.getId()); e.setName(a.getName());
                e.setEmail(Base64Util.encode(a.getEmail())); e.setPassword(a.getPassword());
                e.setUserType(a.getUserType());
                if (a.getAssignedMatches() != null)
                    e.setAssignedMatches(a.getAssignedMatches().stream().map(partidoMapper::toEntity).toList());
                return e;
            }
            public Arbitro toDomain(ArbitroEntity e) {
                if (e == null) return null;
                Arbitro a = new Arbitro();
                a.setId(e.getId()); a.setName(e.getName());
                a.setEmail(Base64Util.decode(e.getEmail())); a.setPassword(e.getPassword());
                a.setUserType(e.getUserType());
                if (e.getAssignedMatches() != null)
                    a.setAssignedMatches(e.getAssignedMatches().stream().map(partidoMapper::toDomain).toList());
                return a;
            }
        };
        ReflectionTestUtils.setField(mapper, "partidoMapper", partidoMapper);
        return mapper;
    }

    public static CapitanMapper capitanMapper(EquipoJpaRepository equipoRepo, EquipoMapper equipoMapper) {
        CapitanMapper mapper = new CapitanMapper() {
            public CapitanEntity toEntity(Capitan c) {
                if (c == null) return null;
                CapitanEntity e = new CapitanEntity();
                e.setId(c.getId()); e.setName(c.getName());
                e.setEmail(Base64Util.encode(c.getEmail())); e.setPassword(c.getPassword());
                e.setUserType(c.getUserType()); e.setJerseyNumber(c.getJerseyNumber());
                e.setPosition(c.getPosition()); e.setAvailable(c.isAvailable());
                e.setPhoto(c.getPhoto()); e.setEquipoId(c.getEquipo());
                if (c.getTeam() != null) e.setTeamId(c.getTeam().getId());
                return e;
            }
            public Capitan toDomain(CapitanEntity e) {
                if (e == null) return null;
                Capitan c = new Capitan();
                c.setId(e.getId()); c.setName(e.getName());
                c.setEmail(Base64Util.decode(e.getEmail())); c.setPassword(e.getPassword());
                c.setUserType(e.getUserType()); c.setJerseyNumber(e.getJerseyNumber());
                c.setPosition(e.getPosition()); c.setAvailable(e.isAvailable());
                c.setPhoto(e.getPhoto()); c.setEquipo(e.getEquipoId());
                if (e.getTeamId() != null)
                    equipoRepo.findById(e.getTeamId()).ifPresent(eq -> c.setTeam(equipoMapper.toDomain(eq)));
                return c;
            }
        };
        ReflectionTestUtils.setField(mapper, "equipoJpaRepository", equipoRepo);
        ReflectionTestUtils.setField(mapper, "equipoMapper", equipoMapper);
        return mapper;
    }

    public static PerfilDeportivoMapper perfilDeportivoMapper() {
        return new PerfilDeportivoMapper() {
            public edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PerfilDeportivoEntity toEntity(edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo p) {
                if (p == null) return null;
                edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PerfilDeportivoEntity e = new edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PerfilDeportivoEntity();
                e.setId(p.getId()); e.setJugadorId(p.getJugadorId());
                e.setPosiciones(p.getPosiciones()); e.setDorsal(p.getDorsal());
                e.setFoto(p.getFoto()); e.setEdad(p.getEdad());
                e.setGenero(p.getGenero());
                e.setIdentificacion(p.getIdentificacion() != null ? Base64Util.encode(p.getIdentificacion()) : null);
                e.setSemestre(p.getSemestre());
                return e;
            }
            public edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo toDomain(edu.dosw.project.SFC_TechUp_Futbol.persistence.entity.PerfilDeportivoEntity e) {
                if (e == null) return null;
                edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo p = new edu.dosw.project.SFC_TechUp_Futbol.core.model.PerfilDeportivo();
                p.setId(e.getId()); p.setJugadorId(e.getJugadorId());
                p.setPosiciones(e.getPosiciones()); p.setDorsal(e.getDorsal());
                p.setFoto(e.getFoto()); p.setEdad(e.getEdad());
                p.setGenero(e.getGenero());
                p.setIdentificacion(e.getIdentificacion() != null ? Base64Util.decode(e.getIdentificacion()) : null);
                p.setSemestre(e.getSemestre());
                return p;
            }
        };
    }
}
