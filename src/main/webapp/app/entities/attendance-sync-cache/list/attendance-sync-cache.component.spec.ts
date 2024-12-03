import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AttendanceSyncCacheService } from '../service/attendance-sync-cache.service';

import { AttendanceSyncCacheComponent } from './attendance-sync-cache.component';
import SpyInstance = jest.SpyInstance;

describe('AttendanceSyncCache Management Component', () => {
  let comp: AttendanceSyncCacheComponent;
  let fixture: ComponentFixture<AttendanceSyncCacheComponent>;
  let service: AttendanceSyncCacheService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'attendance-sync-cache', component: AttendanceSyncCacheComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [AttendanceSyncCacheComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(AttendanceSyncCacheComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendanceSyncCacheComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttendanceSyncCacheService);
    routerNavigateSpy = jest.spyOn(comp.router, 'navigate');

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.attendanceSyncCaches?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to attendanceSyncCacheService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAttendanceSyncCacheIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAttendanceSyncCacheIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it('should load a page', () => {
    // WHEN
    comp.navigateToPage(1);

    // THEN
    expect(routerNavigateSpy).toHaveBeenCalled();
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.navigateToWithComponentValues();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['name,asc'],
        }),
      })
    );
  });

  it('should re-initialize the page', () => {
    // WHEN
    comp.loadPage(1);
    comp.reset();

    // THEN
    expect(comp.page).toEqual(1);
    expect(service.query).toHaveBeenCalledTimes(2);
    expect(comp.attendanceSyncCaches?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
