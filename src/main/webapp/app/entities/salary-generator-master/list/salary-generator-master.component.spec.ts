import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SalaryGeneratorMasterService } from '../service/salary-generator-master.service';

import { SalaryGeneratorMasterComponent } from './salary-generator-master.component';
import SpyInstance = jest.SpyInstance;

describe('SalaryGeneratorMaster Management Component', () => {
  let comp: SalaryGeneratorMasterComponent;
  let fixture: ComponentFixture<SalaryGeneratorMasterComponent>;
  let service: SalaryGeneratorMasterService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'salary-generator-master', component: SalaryGeneratorMasterComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [SalaryGeneratorMasterComponent],
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
      .overrideTemplate(SalaryGeneratorMasterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalaryGeneratorMasterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SalaryGeneratorMasterService);
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
    expect(comp.salaryGeneratorMasters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to salaryGeneratorMasterService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSalaryGeneratorMasterIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSalaryGeneratorMasterIdentifier).toHaveBeenCalledWith(entity);
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
    expect(comp.salaryGeneratorMasters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
