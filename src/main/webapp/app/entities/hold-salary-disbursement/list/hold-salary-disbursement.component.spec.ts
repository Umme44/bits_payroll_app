import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HoldSalaryDisbursementService } from '../service/hold-salary-disbursement.service';

import { HoldSalaryDisbursementComponent } from './hold-salary-disbursement.component';
import SpyInstance = jest.SpyInstance;

describe('HoldSalaryDisbursement Management Component', () => {
  let comp: HoldSalaryDisbursementComponent;
  let fixture: ComponentFixture<HoldSalaryDisbursementComponent>;
  let service: HoldSalaryDisbursementService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'hold-salary-disbursement', component: HoldSalaryDisbursementComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [HoldSalaryDisbursementComponent],
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
      .overrideTemplate(HoldSalaryDisbursementComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HoldSalaryDisbursementComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HoldSalaryDisbursementService);
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
    expect(comp.holdSalaryDisbursements?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to holdSalaryDisbursementService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getHoldSalaryDisbursementIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getHoldSalaryDisbursementIdentifier).toHaveBeenCalledWith(entity);
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
    expect(comp.holdSalaryDisbursements?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
