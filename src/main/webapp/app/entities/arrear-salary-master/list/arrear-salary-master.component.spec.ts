import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ArrearSalaryMasterService } from '../service/arrear-salary-master.service';

import { ArrearSalaryMasterComponent } from './arrear-salary-master.component';
import SpyInstance = jest.SpyInstance;

describe('ArrearSalaryMaster Management Component', () => {
  let comp: ArrearSalaryMasterComponent;
  let fixture: ComponentFixture<ArrearSalaryMasterComponent>;
  let service: ArrearSalaryMasterService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'arrear-salary-master', component: ArrearSalaryMasterComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [ArrearSalaryMasterComponent],
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
      .overrideTemplate(ArrearSalaryMasterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArrearSalaryMasterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ArrearSalaryMasterService);
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
    expect(comp.arrearSalaryMasters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to arrearSalaryMasterService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getArrearSalaryMasterIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getArrearSalaryMasterIdentifier).toHaveBeenCalledWith(entity);
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
    expect(comp.arrearSalaryMasters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
