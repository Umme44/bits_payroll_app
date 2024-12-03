import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AttendanceSummaryService } from '../service/attendance-summary.service';

import { AttendanceSummaryComponent } from './attendance-summary.component';
import SpyInstance = jest.SpyInstance;

describe('AttendanceSummary Management Component', () => {
  let comp: AttendanceSummaryComponent;
  let fixture: ComponentFixture<AttendanceSummaryComponent>;
  let service: AttendanceSummaryService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'attendance-summary', component: AttendanceSummaryComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [AttendanceSummaryComponent],
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
      .overrideTemplate(AttendanceSummaryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendanceSummaryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttendanceSummaryService);
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
    expect(comp.attendanceSummaries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to attendanceSummaryService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAttendanceSummaryIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAttendanceSummaryIdentifier).toHaveBeenCalledWith(entity);
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
    expect(comp.attendanceSummaries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
