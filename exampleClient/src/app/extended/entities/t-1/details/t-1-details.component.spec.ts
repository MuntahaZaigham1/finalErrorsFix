import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { DetailsComponent, ListComponent, FieldsComp } from 'src/app/common/general-components';

import { TestingModule, EntryComponents } from 'src/testing/utils';
import { T1ExtendedService, T1DetailsExtendedComponent, T1ListExtendedComponent } from '../';
import { IT1 } from 'src/app/entities/t-1';
describe('T1DetailsExtendedComponent', () => {
  let component: T1DetailsExtendedComponent;
  let fixture: ComponentFixture<T1DetailsExtendedComponent>;
  let el: HTMLElement;
  
  describe('Unit Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          T1DetailsExtendedComponent,
          DetailsComponent
        ],
        imports: [TestingModule],
        providers: [
          T1ExtendedService,
        ],
        schemas: [NO_ERRORS_SCHEMA]  
      }).compileComponents();
    }));
  
    beforeEach(() => {
      fixture = TestBed.createComponent(T1DetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

  });
  
  describe('Integration Tests', () => {
    beforeEach(async(() => {

      TestBed.configureTestingModule({
        declarations: [
          T1DetailsExtendedComponent,
          T1ListExtendedComponent,
          DetailsComponent,
          ListComponent,
          FieldsComp
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 't1', component: T1DetailsExtendedComponent },
            { path: 't1/:id', component: T1ListExtendedComponent }
          ])
        ],
        providers: [
          T1ExtendedService
        ]

      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(T1DetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

  });
  
});
