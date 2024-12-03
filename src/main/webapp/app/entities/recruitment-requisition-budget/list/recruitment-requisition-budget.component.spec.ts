import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RecruitmentRequisitionBudgetService } from '../service/recruitment-requisition-budget.service';

import { RecruitmentRequisitionBudgetComponent } from './recruitment-requisition-budget.component';
import SpyInstance = jest.SpyInstance;

describe('RecruitmentRequisitionBudget Management Component', () => {
  let comp: RecruitmentRequisitionBudgetComponent;
  let fixture: ComponentFixture<RecruitmentRequisitionBudgetComponent>;
  let service: RecruitmentRequisitionBudgetService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'recruitment-requisition-budget', component: RecruitmentRequisitionBudgetComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [RecruitmentRequisitionBudgetComponent],
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
      .overrideTemplate(RecruitmentRequisitionBudgetComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecruitmentRequisitionBudgetComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RecruitmentRequisitionBudgetService);
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
    expect(comp.recruitmentRequisitionBudgets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to recruitmentRequisitionBudgetService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getRecruitmentRequisitionBudgetIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getRecruitmentRequisitionBudgetIdentifier).toHaveBeenCalledWith(entity);
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
});
