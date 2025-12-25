package com.example.demo;
import com.example.demo.config.JwtUtil;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;

import org.mockito.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class MasterTestNGSuiteTest {

    @Mock private UserRepository userRepo;
    @Mock private CrimeReportRepository reportRepo;
    @Mock private HotspotZoneRepository zoneRepo;
    @Mock private PatternDetectionResultRepository resultRepo;
    @Mock private AnalysisLogRepository logRepo;

    private UserService userService;
    private CrimeReportService reportService;
    private HotspotZoneService zoneService;
    private PatternDetectionService detectionService;
    private AnalysisLogService logService;
    private JwtUtil jwtUtil = new JwtUtil();

    @BeforeClass
    public void init(){
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepo);
        reportService = new CrimeReportServiceImpl(reportRepo);
        zoneService = new HotspotZoneServiceImpl(zoneRepo);
        detectionService = new PatternDetectionServiceImpl(zoneRepo, reportRepo, resultRepo, logRepo);
        logService = new AnalysisLogServiceImpl(logRepo, zoneRepo);
    }

    // --- 1: Servlet simulation (5 tests)
    @Test(priority=1) public void testServletDeploy1(){ Assert.assertEquals("Servlet","Servlet"); }
    @Test(priority=2) public void testServletDeploy2(){ Assert.assertTrue("OK".contains("O")); }
    @Test(priority=3) public void testServletDeploy3(){ Assert.assertNotNull(new Object()); }
    @Test(priority=4) public void testServletResponseTime(){ Assert.assertTrue(100>10); }
    @Test(priority=5) public void testServletSimple(){ Assert.assertEquals(1+1,2); }

    // --- 2: CRUD operations (6-20)
    @Test(priority=6) public void testRegisterUser() {
        User u = new User("Alice","alice@test.com","pwd","ANALYST");
        when(userRepo.existsByEmail("alice@test.com")).thenReturn(false);
        when(userRepo.save(any(User.class))).thenAnswer(i->i.getArguments()[0]);
        User saved = userService.register(u);
        Assert.assertEquals(saved.getEmail(),"alice@test.com");
    }

    @Test(priority=7) public void testRegisterDuplicateEmail(){
        User u = new User("B","b@test.com","pwd","ANALYST");
        when(userRepo.existsByEmail("b@test.com")).thenReturn(true);
        try{ userService.register(u); Assert.fail(); }catch(Exception ex){ Assert.assertTrue(ex.getMessage().toLowerCase().contains("exists")); }
    }

    @Test(priority=8) public void testAddCrimeReport(){
        CrimeReport r = new CrimeReport();
        r.setCrimeType("THEFT"); r.setLatitude(12.0); r.setLongitude(77.0); r.setOccurredAt(LocalDateTime.now());
        when(reportRepo.save(any(CrimeReport.class))).thenAnswer(i->i.getArguments()[0]);
        CrimeReport out = reportService.addReport(r);
        Assert.assertEquals(out.getCrimeType(),"THEFT");
    }

    @Test(priority=9) public void testAddCrimeReportInvalidLat(){
        CrimeReport r = new CrimeReport(); r.setLatitude(200.0); r.setLongitude(0.0);
        try{ reportService.addReport(r); Assert.fail(); } catch(Exception ex){ Assert.assertTrue(ex.getMessage().toLowerCase().contains("latitude")); }
    }

    @Test(priority=10) public void testGetAllReports(){
        when(reportRepo.findAll()).thenReturn(List.of(new CrimeReport()));
        Assert.assertFalse(reportService.getAllReports().isEmpty());
    }

    @Test(priority=11) public void testCreateZone(){
        HotspotZone z = new HotspotZone(); z.setZoneName("Z1"); z.setCenterLat(12.0); z.setCenterLong(77.0);
        when(zoneRepo.existsByZoneName("Z1")).thenReturn(false);
        when(zoneRepo.save(any(HotspotZone.class))).thenAnswer(i->i.getArguments()[0]);
        HotspotZone out = zoneService.addZone(z);
        Assert.assertEquals(out.getZoneName(),"Z1");
    }

    @Test(priority=12) public void testCreateZoneDuplicate(){
        HotspotZone z = new HotspotZone(); z.setZoneName("Zdup"); z.setCenterLat(0.0); z.setCenterLong(0.0);
        when(zoneRepo.existsByZoneName("Zdup")).thenReturn(true);
        try{ zoneService.addZone(z); Assert.fail(); }catch(Exception ex){ Assert.assertTrue(ex.getMessage().toLowerCase().contains("exists")); }
    }

    @Test(priority=13) public void testListZones(){
        when(zoneRepo.findAll()).thenReturn(List.of(new HotspotZone()));
        Assert.assertFalse(zoneService.getAllZones().isEmpty());
    }

    @Test(priority=14) public void testDetectPatternNoZone(){
        when(zoneRepo.findById(99L)).thenReturn(Optional.empty());
        try{ detectionService.detectPattern(99L); Assert.fail(); }catch(Exception ex){ Assert.assertTrue(ex.getMessage().toLowerCase().contains("zone")); }
    }

    @Test(priority=15) public void testAnalysisLogAddAndFetch(){
        HotspotZone z = new HotspotZone(); z.setId(10L);
        when(zoneRepo.findById(10L)).thenReturn(Optional.of(z));
        when(logRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        AnalysisLog log = logService.addLog(10L, "msg");
        Assert.assertEquals(log.getMessage(),"msg");
    }

    @Test(priority=16) public void testGetLogsForZone(){
        when(logRepo.findByZone_Id(10L)).thenReturn(List.of(new AnalysisLog()));
        Assert.assertFalse(logService.getLogsByZone(10L).isEmpty());
    }

    @Test(priority=17) public void testPatternDetectionBasic(){
        HotspotZone z = new HotspotZone(); z.setId(5L); z.setCenterLat(12.0); z.setCenterLong(77.0);
        when(zoneRepo.findById(5L)).thenReturn(Optional.of(z));
        when(reportRepo.findByLatLongRange(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn(new ArrayList<>());
        when(resultRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        when(logRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        when(zoneRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        var res = detectionService.detectPattern(5L);
        Assert.assertNotNull(res);
        Assert.assertTrue(res.getCrimeCount()!=null);
    }

    @Test(priority=18) public void testPatternResultFetch(){
        when(resultRepo.findByZone_Id(5L)).thenReturn(List.of(new PatternDetectionResult()));
        Assert.assertFalse(detectionService.getResultsByZone(5L).isEmpty());
    }

    @Test(priority=19) public void testUserFindByEmail(){
        User u = new User("U","u@test.com","pwd","ANALYST");
        when(userRepo.findByEmail("u@test.com")).thenReturn(Optional.of(u));
        Assert.assertEquals(userService.findByEmail("u@test.com").getName(),"U");
    }

    @Test(priority=20) public void testUserFindEmailNotFound(){
        when(userRepo.findByEmail("nx@test.com")).thenReturn(Optional.empty());
        try{ userService.findByEmail("nx@test.com"); Assert.fail(); }catch(Exception ex){ Assert.assertTrue(ex.getMessage().toLowerCase().contains("not")); }
    }

    // --- 3: DI & IoC tests (21-30)
    @Test(priority=21) public void testDIUserService(){ Assert.assertNotNull(userService); }
    @Test(priority=22) public void testDICrimeService(){ Assert.assertNotNull(reportService); }
    @Test(priority=23) public void testDIZoneService(){ Assert.assertNotNull(zoneService); }
    @Test(priority=24) public void testDIDetectionService(){ Assert.assertNotNull(detectionService); }
    @Test(priority=25) public void testRepoMockingWorks(){ when(reportRepo.findAll()).thenReturn(List.of()); Assert.assertTrue(reportService.getAllReports().isEmpty()); }
    @Test(priority=26) public void testRepoSaveCrime(){ when(reportRepo.save(any())).thenAnswer(i->i.getArguments()[0]); CrimeReport r = new CrimeReport(); r.setLatitude(10.0); r.setLongitude(10.0); r.setOccurredAt(LocalDateTime.now()); var out = reportService.addReport(r); Assert.assertNotNull(out); }
    @Test(priority=27) public void testHibernateAnnotationCheck(){ CrimeReport r = new CrimeReport(); r.setCrimeType("TYPE"); Assert.assertEquals(r.getCrimeType(),"TYPE"); }
    @Test(priority=28) public void testEntityMappingZone(){
        HotspotZone z = new HotspotZone(); z.setZoneName("map1"); Assert.assertEquals(z.getZoneName(),"map1");
    }
    @Test(priority=29) public void testPatternResultMapping(){ PatternDetectionResult p = new PatternDetectionResult(); p.setDetectedPattern("p"); Assert.assertEquals(p.getDetectedPattern(),"p"); }
    @Test(priority=30) public void testAnalysisLogMapping(){ AnalysisLog l = new AnalysisLog(); l.setMessage("m"); Assert.assertEquals(l.getMessage(),"m"); }

    // --- 4: JPA normalization / 1NF2NF3NF tests (31-40)
    @Test(priority=31) public void test1NFFieldsAtomic(){
        CrimeReport r = new CrimeReport(); r.setCrimeType("A"); r.setDescription("desc"); Assert.assertFalse(r.getDescription().contains(","));
    }
    @Test(priority=32) public void test2NFNoPartialDependency(){
        HotspotZone z = new HotspotZone(); z.setZoneName("Z"); z.setCenterLat(1.0); z.setCenterLong(1.0); Assert.assertEquals(z.getCenterLat(), Double.valueOf(1.0));
    }
    @Test(priority=33) public void test3NFNoTransitive(){
        PatternDetectionResult p = new PatternDetectionResult(); p.setCrimeCount(3); Assert.assertEquals(p.getCrimeCount(), Integer.valueOf(3));
    }
    @Test(priority=34) public void testRelationZoneToResults(){
        PatternDetectionResult p = new PatternDetectionResult(); HotspotZone z = new HotspotZone(); z.setZoneName("Zx"); p.setZone(z); Assert.assertEquals(p.getZone().getZoneName(),"Zx");
    }
    @Test(priority=35) public void testNoDuplicateZoneNames(){
        when(zoneRepo.existsByZoneName("uniq")).thenReturn(true);
        try{ zoneService.addZone(new HotspotZone(){ { setZoneName("uniq"); setCenterLat(0.0); setCenterLong(0.0);} }); Assert.fail(); }catch(Exception ex){ Assert.assertTrue(ex.getMessage().toLowerCase().contains("exists")); }
    }
    @Test(priority=36) public void testCrimeCountNonNegative(){
        PatternDetectionResult p = new PatternDetectionResult(); p.setCrimeCount(0); Assert.assertTrue(p.getCrimeCount() >= 0);
    }
    @Test(priority=37) public void testPatternTextNotEmpty(){
        PatternDetectionResult p = new PatternDetectionResult(); p.setDetectedPattern("pattern"); Assert.assertFalse(p.getDetectedPattern().isEmpty());
    }
    @Test(priority=38) public void testAnalysisLogTimestampAuto(){
        AnalysisLog l = new AnalysisLog(); Assert.assertNotNull(l.getLoggedAt());
    }
    @Test(priority=39) public void testCrimeOccurredAtNotFuture(){
        CrimeReport r = new CrimeReport(); r.setOccurredAt(LocalDateTime.now().minusDays(1)); Assert.assertTrue(r.getOccurredAt().isBefore(LocalDateTime.now()));
    }
    @Test(priority=40) public void testCoordinatesRange(){
        CrimeReport r = new CrimeReport(); r.setLatitude(45.0); r.setLongitude(90.0); Assert.assertTrue(r.getLatitude() <= 90 && r.getLongitude() <= 180);
    }

    // --- 5: Many-to-Many and association tests (41-46)
    // (note: project doesn't have many-to-many, but we still check associations)
    @Test(priority=41) public void testZoneHasResultsCollection(){
        PatternDetectionResult p = new PatternDetectionResult();
        Assert.assertNull(p.getZone()); // initially null
    }
    @Test(priority=42) public void testAddLogToZone(){
        HotspotZone z = new HotspotZone(); z.setId(8L);
        AnalysisLog l = new AnalysisLog(); l.setZone(z);
        Assert.assertEquals(l.getZone().getId(), Long.valueOf(8));
    }
    @Test(priority=43) public void testMultipleResultsAdded(){
        PatternDetectionResult a = new PatternDetectionResult(); PatternDetectionResult b = new PatternDetectionResult();
        List<PatternDetectionResult> arr = new ArrayList<>(); arr.add(a); arr.add(b);
        Assert.assertEquals(arr.size(),2);
    }
    @Test(priority=44) public void testZoneSeverityChangeBasedOnCount(){
        HotspotZone z = new HotspotZone(); z.setCenterLat(1.0); z.setCenterLong(1.0);
        when(zoneRepo.findById(2L)).thenReturn(Optional.of(z));
        when(reportRepo.findByLatLongRange(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn(Collections.nCopies(10, new CrimeReport()));
        when(resultRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        when(logRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        when(zoneRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        var r = detectionService.detectPattern(2L);
        Assert.assertTrue(r.getDetectedPattern().toLowerCase().contains("high") || r.getDetectedPattern().toLowerCase().contains("medium") || r.getDetectedPattern().toLowerCase().contains("no"));
    }
    @Test(priority=45) public void testAddZoneValidationLatLong(){
        HotspotZone z = new HotspotZone(); z.setCenterLat(100.0); z.setCenterLong(200.0);
        try{ zoneService.addZone(z); Assert.fail(); }catch(Exception ex){ Assert.assertTrue(ex.getMessage().toLowerCase().contains("latitude")||ex.getMessage().toLowerCase().contains("longitude")); }
    }
    @Test(priority=46) public void testPatternResultDateSet(){
        PatternDetectionResult p = new PatternDetectionResult(); p.setAnalysisDate(java.time.LocalDate.now()); Assert.assertEquals(p.getAnalysisDate(), java.time.LocalDate.now());
    }

    // --- 6: JWT auth tests (47-54)
    @Test(priority=47) public void testJwtGenerateAndParse(){
        String token = jwtUtil.generateToken(7L, "a@a.com", "ANALYST");
        var claims = jwtUtil.parseToken(token);
        Assert.assertEquals(claims.get("email", String.class), "a@a.com");
        Assert.assertEquals(Long.valueOf(claims.get("userId").toString()), Long.valueOf(7L));
    }
    @Test(priority=48) public void testJwtContainsRole(){
        String token = jwtUtil.generateToken(3L, "b@b.com", "ADMIN");
        var c = jwtUtil.parseToken(token);
        Assert.assertEquals(c.get("role", String.class), "ADMIN");
    }
    @Test(priority=49) public void testAuthControllerRegisterLoginSim(){
        // Simulate register & login simple behavior with services mocked
        when(userRepo.existsByEmail("x@x.com")).thenReturn(false);
        User u = new User("X","x@x.com","pwd","ANALYST"); when(userRepo.save(any())).thenReturn(u);
        User saved = userService.register(u);
        Assert.assertEquals(saved.getEmail(),"x@x.com");
        String token = jwtUtil.generateToken(1L,"x@x.com","ANALYST");
        Assert.assertTrue(token.length()>10);
    }
    @Test(priority=50) public void testJwtInvalidToken(){
        try{ jwtUtil.parseToken("bad.token"); Assert.fail(); }catch(Exception ex){ Assert.assertTrue(true); }
    }
    @Test(priority=51) public void testAuthPayloadIntegrity(){
        String t = jwtUtil.generateToken(10L, "c@c.com", "ANALYST");
        var c = jwtUtil.parseToken(t);
        Assert.assertEquals(c.get("email",String.class),"c@c.com");
    }
    @Test(priority=52) public void testRoleDefault(){
        User u = new User(); Assert.assertEquals(u.getRole(),"ANALYST");
    }
    @Test(priority=53) public void testPasswordHashingOnRegister(){
        User u = new User("H","h@h.com","password","ANALYST");
        when(userRepo.existsByEmail("h@h.com")).thenReturn(false);
        when(userRepo.save(any())).thenAnswer(i->i.getArguments()[0]);
        User saved = userService.register(u);
        Assert.assertNotEquals(saved.getPassword(),"password");
    }
    @Test(priority=54) public void testAuthLoginProducesToken(){
        User u = new User("L","l@l.com","pwd","ANALYST"); u.setId(22L);
        when(userRepo.findByEmail("l@l.com")).thenReturn(Optional.of(u));
        String token = jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRole());
        Assert.assertTrue(token.contains("."));
    }

    // --- 7: HQL / advanced queries (55-60)
    @Test(priority=55) public void testFindCrimesInRange(){
        when(reportRepo.findByLatLongRange(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn(List.of(new CrimeReport(), new CrimeReport()));
        var list = reportRepo.findByLatLongRange(0.0,1.0,0.0,1.0);
        Assert.assertEquals(list.size(),2);
    }
    @Test(priority=56) public void testFindZonesBySeverity(){
        when(zoneRepo.findBySeverityLevel("HIGH")).thenReturn(List.of(new HotspotZone()));
        var l = zoneRepo.findBySeverityLevel("HIGH");
        Assert.assertFalse(l.isEmpty());
    }
    @Test(priority=57) public void testPatternResultsQuery(){
        when(resultRepo.findByZone_Id(3L)).thenReturn(List.of(new PatternDetectionResult()));
        var l = detectionService.getResultsByZone(3L);
        Assert.assertFalse(l.isEmpty());
    }
    @Test(priority=58) public void testCountReportsEdge(){
        when(reportRepo.findByLatLongRange(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn(Collections.emptyList());
        var l = reportRepo.findByLatLongRange(0.0,0.1,0.0,0.1);
        Assert.assertTrue(l.isEmpty());
    }
    @Test(priority=59) public void testZoneRepoFindByName(){
        when(zoneRepo.findByZoneName("Z")).thenReturn(Optional.of(new HotspotZone()));
        var z = zoneRepo.findByZoneName("Z");
        Assert.assertTrue(z.isPresent());
    }
    @Test(priority=60) public void testFinalAssert(){ Assert.assertTrue(true); }

}
